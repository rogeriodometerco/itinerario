package facade;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.FechamentoRota;
import modelo.Rota;
import dao.FechamentoRotaDao;

@Stateless
public class FechamentoRotaFacade 
extends GenericCrudFacade<FechamentoRota> {

	@EJB
	private FechamentoRotaDao dao;

	@Override
	protected FechamentoRotaDao getDao() {
		return dao;
	}

	public FechamentoRota recuperarFechamentoRota(Rota rota, Date dataInicial, Date dataFinal)
			throws Exception {
		// TODO
		return null;
	}
}