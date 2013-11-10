package facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Linha;
import dao.LinhaDao;

@Stateless
public class LinhaFacade
	extends GenericCrudFacade<Linha> {
	
	@EJB
	private LinhaDao dao;
	
	@Override
	protected LinhaDao getDao() {
		return dao;
	}
	
	public Linha recuperarParaEdicao(Long id) throws Exception {
		// TODO Refactoring. Esta não é a melhor forma.
		Linha linha = dao.recuperar(id);
		linha.getPontos().size();
		linha.getAgendamentos().size();
		return linha;
	}

}
