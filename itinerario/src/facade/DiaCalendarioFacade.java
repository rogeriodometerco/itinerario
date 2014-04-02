package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.Calendario;
import modelo.DiaCalendario;
import dao.DiaCalendarioDao;

@Stateless
public class DiaCalendarioFacade
extends GenericCrudFacade<DiaCalendario> {

	@EJB
	private DiaCalendarioDao dao;

	@Override
	protected DiaCalendarioDao getDao() {
		return dao;
	}

	public List<DiaCalendario> listar(Calendario calendario) 
			throws Exception {
		String sql = "select x"
				+ " from DiaCalendario x "
				+ " where x.calendario = :calendario";
		return getEntityManager().createQuery(sql, DiaCalendario.class)
				.setParameter("calendario", calendario)
				.getResultList();
	}
	
	public DiaCalendario recuperar(Calendario calendario, Date data) {
		String sql = "select x"
				+ " from DiaCalendario x "
				+ " where x.calendario = :calendario"
				+ " and x.data = :data";
		try {
			return getEntityManager().createQuery(sql, DiaCalendario.class)
				.setParameter("calendario", calendario)
				.setParameter("data", data)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}