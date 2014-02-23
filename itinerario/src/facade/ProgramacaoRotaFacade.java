package facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;

import managedbean.AppException;
import modelo.ProgramacaoRota;
import modelo.Rota;
import modelo.Veiculo;
import dao.ProgramacaoRotaDao;


@Stateless
public class ProgramacaoRotaFacade 
extends GenericCrudFacade<ProgramacaoRota> {

	@EJB
	private ProgramacaoRotaDao dao;
	@EJB
	private CalendarioLetivoFacade calendarioFacade;

	@Override
	protected ProgramacaoRotaDao getDao() {
		return dao;
	}
	/**
	 * Recupera as programa��es de rota para uma data.
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		String sql = "select p from ProgramacaoRota as p"
				+ " where p.inicioVigencia <= :data and p.terminoVigencia >= :data";
		List<ProgramacaoRota> lista = (List<ProgramacaoRota>)getEntityManager()
				.createQuery(sql)
				.setParameter("data", data, TemporalType.TIMESTAMP)
				.getResultList();
		// Se a programa��o � baseada no calend�rio letivo, verifica se no calend�rio a data � dia �til.
		// Caso negativo, a programa��o � retirada da lista.

		//TODO descomentar trecho abaixo quando o calend�rio estiver sendo informado na programa��o da rota.
		// Em lista.remove(prog) est� tamb�m dando erro - Concurrency...Modifycation
		/*
		for (ProgramacaoRota prog: lista) {
			if (prog.getTipoCalendario().equals(CalendarioEnum.LETIVO) && prog.getCalendarioLetivo() != null) {
				if (!calendarioFacade.isDiaLetivo(prog.getCalendarioLetivo(), data)) {
					lista.remove(prog);
				}
			}
		}*/
		return lista;
	}

	public List<ProgramacaoRota> listar(Rota rota) 
			throws Exception {
		String sql = "select x"
				+ " from ProgramacaoRota as x" 
				+ " where x.rota = :rota";

		return getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<ProgramacaoRota> listar(Veiculo veiculo) 
			throws Exception {
		String sql = "select x"
				+ " from ProgramacaoRota as x" 
				+ " where x.veiculo = :veiculo";

		return getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	public List<ProgramacaoRota> listar(Rota rota, Veiculo veiculo) 
			throws Exception {
		String sql = "select x"
				+ " from ProgramacaoRota as x" 
				+ " where x.rota = :rota"
				+ " and x.veiculo = :veiculo";

		return getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("rota", rota)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	public List<ProgramacaoRota> autocomplete(String chave) 
			throws Exception {
		String sql = "select p"
				+ " from ProgramacaoRota as p, Rota as r" 
				+ " where p.rota = r"
				+ " and ("
				+ "	upper(r.codigo) like :chave"
				+ "	or upper(r.nome) like :chave"
				+ "	or upper(r.origem) like :chave"
				+ "	or upper(r.destino) like :chave"
				+ " )";

		return getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.getResultList();
	}

	@Override
	protected void validar(ProgramacaoRota p) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (p.getRota() == null) {
			erros.add("Informe a rota");
		}
		if (p.getVeiculo() == null) {
			erros.add("Informe o ve�culo");
		}
		if (p.getHoraInicial() == null) {
			erros.add("Informe a hora inicial");
		}
		if (p.getHoraFinal() == null) {
			erros.add("Informe a hora final");
		}
		if (p.getCalendario() == null) {
			erros.add("Informe o calend�rio");
		}
		if (p.getInicioVigencia() == null) {
			erros.add("Informe a data de in�cio da vig�ncia");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}
}