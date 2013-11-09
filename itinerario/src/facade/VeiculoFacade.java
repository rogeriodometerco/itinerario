package facade;

import javax.ejb.EJB;

import modelo.Veiculo;
import dao.VeiculoDao;

public class VeiculoFacade
	extends GenericCrudFacade<Veiculo> {
	
	@EJB
	private VeiculoDao dao;
	
	@Override
	protected VeiculoDao getDao() {
		return dao;
	}

}
