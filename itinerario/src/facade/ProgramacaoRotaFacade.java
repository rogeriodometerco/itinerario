package facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

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
	private CalendarioFacade calendarioFacade;

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
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		List<ProgramacaoRota> lista = new ArrayList<ProgramacaoRota>();
		String sql = "select p from ProgramacaoRota as p"
				+ " where p.inicioPeriodo <= :data and p.terminoPeriodo >= :data";
		for (ProgramacaoRota p: getEntityManager()
				.createQuery(sql, ProgramacaoRota.class)
				.setParameter("data", data)
				.getResultList()) {
			if (p.getCalendario() == null || calendarioFacade.isDiaUtil(p.getCalendario(), data)) {
				lista.add(p);
			}
		}
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
			erros.add("Informe o veículo");
		}
		if (p.getMotorista() == null) {
			erros.add("Informe o motorista");
		}
		if (p.getHoraInicial() == null) {
			erros.add("Informe a hora inicial");
		}
		if (p.getHoraFinal() == null) {
			erros.add("Informe a hora final");
		}
		if (p.getCalendario() == null) {
			erros.add("Informe o calendário");
		}
		if (p.getInicioPeriodo() == null) {
			erros.add("Informe a data de início do período");
		}
		if (p.getInicioPeriodo() != null && p.getTerminoPeriodo() != null) {
			if (p.getTerminoPeriodo().before(p.getInicioPeriodo())) {
				erros.add("Data final não pode ser menor que a data inicial");
			}
		}
		if (p.getHoraFinal() != null && p.getHoraInicial() != null
				&& p.getHoraFinal().before(p.getHoraInicial())) {
			erros.add("Hora final não pode ser menor que a hora inicial");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}
}