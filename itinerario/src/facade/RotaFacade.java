package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Rota;
import modelo.Veiculo;
import dao.RotaDao;

@Stateless
public class RotaFacade
	extends GenericCrudFacade<Rota> {
	
	@EJB
	private RotaDao dao;
	
	@Override
	protected RotaDao getDao() {
		return dao;
	}
	
	public Rota recuperarParaEdicao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}
	 
	public Rota recuperarParaExclusao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}

	private Rota recuperarParaEdicaoOuExclusao(Long id) throws Exception {
		// TODO Refactoring. Esta não é a melhor forma.
		Rota rota = dao.recuperar(id);
		rota.getPontos().size();
		rota.getProgramacoes().size();
		return rota;
	}
	
	public List<Rota> pesquisar(String chave) 
			throws Exception {
		TypedQuery<Rota> query = getEntityManager().createQuery(
				"select x from Rota x where (" +
						"upper(x.codigo) like :chave" +
						" or upper(x.nome) like :chave" +
				")", Rota.class);
		query.setParameter("chave", "%" + chave.toUpperCase() + "%");
		return query.getResultList();
	}
	 
	public Rota recuperarPorCodigo(String codigo) 
			throws Exception {
		String sql = "select r from Rota as r"
				+ " where upper(r.codigo) = :codigo";
		try {
			return (Rota)getEntityManager().createQuery(sql)
					.setParameter("codigo", codigo.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

}
