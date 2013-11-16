package facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

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
				+ " where v.identificacao = :identificacao";
		try {
			return (Veiculo)getEntityManager().createQuery(sql)
				.setParameter("identificacao", identificacao)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

}
