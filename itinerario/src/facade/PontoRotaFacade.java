package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.PontoRota;
import modelo.Rota;
import dao.PontoRotaDao;

@Stateless
public class PontoRotaFacade extends GenericCrudFacade<PontoRota> {

	@EJB
	private PontoRotaDao dao;

	public List<PontoRota> recuperarPontos(Rota rota) throws Exception {
		String sql = "select p"
				+ " from PontoRota as p" 
				+ " where p.rota = :rota"
				+ " order by sequencia";

		return getEntityManager().createQuery(sql, PontoRota.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<PontoRota> recuperarParadas(Rota rota) throws Exception {
		String sql = "select p"
				+ " from PontoRota as p" 
				+ " where p.rota = :rota"
				+ " and p.parada = true"
				+ " order by sequencia";
		/*System.out.println("recuperarParadas() rota " + rota.getId() + " " + getEntityManager().createQuery(sql, PontoRota.class)
		.setParameter("rota", rota)
		.getResultList().size());*/
		
		return getEntityManager().createQuery(sql, PontoRota.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public PontoRota recuperarParada(Rota rota, int numeroParada) throws Exception {
		String sql = "select p"
				+ " from PontoRota as p" 
				+ " where p.rota = :rota"
				+ " and p.numeroParada = :numeroParada"
				+ " order by sequencia";

		return getEntityManager().createQuery(sql, PontoRota.class)
				.setParameter("rota", rota)
				.setParameter("numeroParada", numeroParada)
				.getSingleResult();
	}

	@Override
	protected PontoRotaDao getDao() {
		return dao;
	}

}