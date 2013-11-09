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
		//TODO m�todo para recuperar linha e seus filhos, para edi��o na p�gina. 
		return dao.recuperar(id);
	}

}
