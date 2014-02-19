package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Atendido;
import modelo.EscolaRota;
import modelo.Pessoa;
import modelo.ProgramacaoRota;
import modelo.Rota;
import dao.AtendidoDao;

@Stateless
public class AtendidoFacade
extends GenericCrudFacade<Atendido> {

	@EJB
	private AtendidoDao dao;

	@Override
	protected AtendidoDao getDao() {
		return dao;
	}

	public List<Atendido> listar(Rota rota) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota";

		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota";

		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.getResultList();
	}

	public List<Atendido> listar(Pessoa pessoa) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.pessoa = :pessoa";

		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("pessoa", pessoa)
				.getResultList();
	}

	public List<Atendido> listar(Rota rota, Pessoa pessoa) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota"
				+ " and a.pessoa = :pessoa";

		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.setParameter("pessoa", pessoa)
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota, Pessoa pessoa) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota"
				+ " and x.pessoa = :pessoa";

		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.setParameter("pessoa", pessoa)
				.getResultList();
	}

	@Override
	protected void validar(Atendido entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getProgramacaoRota() == null) {
			erros.add("Informe a programação da rota");
		}
		if (entidade.getPessoa() == null) {
			erros.add("Informe a pessoa");
		}
		if (entidade.getPontoParada() == null) {
			erros.add("Informe o ponto de parada");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}