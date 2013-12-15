package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Veiculo;
import dao.VeiculoDao;

@Stateless
public class VeiculoFacade
extends GenericCrudFacade<Veiculo> {

	@EJB
	private VeiculoDao dao;

	@Override
	protected VeiculoDao getDao() {
		return dao;
	}

	public Veiculo recuperarPorIdentificacao(String identificacao) 
			throws Exception {
		System.out.println("recuperarPorIdentificacao() " + identificacao);
		String sql = "select v from Veiculo as v"
				+ " where upper(v.identificacao) = :identificacao";
		try {
			return (Veiculo)getEntityManager().createQuery(sql)
					.setParameter("identificacao",identificacao.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	public List<Veiculo> listarPorParteDaIdentificacao(String parteDaIdentificacao) 
			throws Exception {

		TypedQuery<Veiculo> query = getEntityManager().createQuery(
				"select x from Veiculo x where upper(x.identificacao) like :identificacao", Veiculo.class);
		query.setParameter("identificacao", "%" + parteDaIdentificacao.toUpperCase() + "%");
		return query.getResultList();
	}

}
