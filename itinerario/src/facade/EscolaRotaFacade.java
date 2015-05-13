package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Escola;
import modelo.EscolaRota;
import modelo.Rota;
import util.Paginador;
import dao.EscolaRotaDao;

@Stateless
public class EscolaRotaFacade extends GenericCrudFacade<EscolaRota> {

	@EJB
	private EscolaRotaDao escolaRotaDao;
	
	@Override
	protected EscolaRotaDao getDao() {
		return escolaRotaDao;
	}

	public List<EscolaRota> listar(Rota rota) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.rota = :rota";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<EscolaRota> listar(Rota rota, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.rota = :rota";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("rota", rota)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<EscolaRota> listar(Escola escola) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.escola = :escola";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("escola", escola)
				.getResultList();
	}

	public List<EscolaRota> listar(Escola escola, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.escola = :escola";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("escola", escola)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<EscolaRota> listar(Rota rota, Escola escola) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.rota = :rota"
				+ " and x.escola = :escola";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("escola", escola)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<EscolaRota> listar(Rota rota, Escola escola, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from EscolaRota as x" 
				+ " where x.rota = :rota"
				+ " and x.escola = :escola";
		return getEntityManager()
				.createQuery(sql, EscolaRota.class)
				.setParameter("escola", escola)
				.setParameter("rota", rota)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	@Override
	protected void validar(EscolaRota entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getEscola() == null) {
			erros.add("Informe a escola");
		}
		if (entidade.getRota() == null) {
			erros.add("Informe a rota");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}
