package facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Rota;
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
	
	 
}
