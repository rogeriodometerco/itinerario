package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;

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
	 * Recupera as programações de rota para uma data.
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		String sql = "select p from ProgramacaoRota as p"
				+ " where p.inicioPeriodo <= :data and p.terminoPeriodo >= :data";
		List<ProgramacaoRota> lista = (List<ProgramacaoRota>)getEntityManager()
				.createQuery(sql)
				.setParameter("data", data, TemporalType.TIMESTAMP)
				.getResultList();
		// Se a programação é baseada no calendário letivo, verifica se no calendário a data é dia útil.
		// Caso negativo, a programação é retirada da lista.

		//TODO descomentar trecho abaixo quando o calendário estiver sendo informado na programação da rota.
		// Em lista.remove(prog) está também dando erro - Concurrency...Modifycation
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

	public List<ProgramacaoRota> listarAutocomplete(String chave) 
			throws Exception {
		// TODO Filtrar por horário também.
		String sql = "select x"
				+ " from ProgramacaoRota as p, Rota as r" 
				+ " where p.rota = r"
				+ " and ("
				+ "	upper(r.codigo) like :chave"
				+ "	or upper(r.nome) like :chave"
				+ " )";

		return getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("chave", chave)
				.getResultList();
	}

}