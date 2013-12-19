package facade;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.CalendarioLetivo;
import dao.CalendarioLetivoDao;

@Stateless
public class CalendarioLetivoFacade
extends GenericCrudFacade<CalendarioLetivo> {

	@EJB
	private CalendarioLetivoDao dao;

	@Override
	protected CalendarioLetivoDao getDao() {
		return dao;
	}

	public boolean isDiaLetivo(CalendarioLetivo calendario, Date data) {
		// TODO 
		return true;
	}
}
