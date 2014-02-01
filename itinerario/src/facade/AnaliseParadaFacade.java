package facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.AnaliseParada;
import modelo.AnaliseViagem;
import modelo.PontoRota;
import dao.AnaliseParadaDao;

@Stateless
public class AnaliseParadaFacade 
extends GenericCrudFacade<AnaliseParada> {

	@EJB
	private AnaliseParadaDao dao;

	@Override
	protected AnaliseParadaDao getDao() {
		return dao;
	}

	/**
	 * Retorna a análise para um ponto de parada se existir.
	 * @param pontoParada
	 * @return
	 * @throws Exception
	 */
	public AnaliseParada recuperarAnaliseParada(AnaliseViagem analiseViagem, PontoRota pontoParada) throws Exception {
		try {
			String sql = "select x"
					+ " from AnaliseParada as x" 
					+ " where x.analiseViagem = :analiseViagem"
					+ " and x.pontoParada = :pontoParada";

			return (AnaliseParada) getEntityManager().createQuery(sql)
					.setParameter("analiseViagem", analiseViagem)
					.setParameter("pontoParada", pontoParada)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}