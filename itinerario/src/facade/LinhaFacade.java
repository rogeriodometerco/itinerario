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
		//TODO método para recuperar linha e seus filhos, para edição na página. 
		return dao.recuperar(id);
	}

}
