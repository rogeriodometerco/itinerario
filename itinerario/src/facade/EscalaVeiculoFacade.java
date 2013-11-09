package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.EscalaVeiculo;
import dao.EscalaVeiculoDao;


@Stateless
public class EscalaVeiculoFacade 
	extends GenericCrudFacade<EscalaVeiculo> {

	@EJB
	private EscalaVeiculoDao dao;
	
	@Override
	protected EscalaVeiculoDao getDao() {
		return dao;
	}
	
	public List<EscalaVeiculo> recuperarEscalas(Date data) throws Exception {
		// TODO filtro considerando a data.
		return listar();
	}

}