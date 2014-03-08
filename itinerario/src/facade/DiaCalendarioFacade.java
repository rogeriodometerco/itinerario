package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

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
	
}