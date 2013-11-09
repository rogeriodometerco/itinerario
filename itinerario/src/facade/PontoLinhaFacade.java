package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dao.PontoLinhaDao;
import modelo.Linha;
import modelo.PontoLinha;

@Stateless
public class PontoLinhaFacade extends GenericCrudFacade<PontoLinha> {

	@EJB
	private PontoLinhaDao dao;

	@SuppressWarnings("unchecked")
	public List<PontoLinha> recuperarPontos(Linha linha) {
		String sql = "select p"
				+ " from PontoLinha as p" 
				+ " where p.linha = :linha"
				+ " order by sequencia";

		return (List<PontoLinha>) getEntityManager().createQuery(sql)
				.setParameter("linha", linha)
				.getResultList();

	}

	@Override
	protected PontoLinhaDao getDao() {
		return dao;
	}

}
