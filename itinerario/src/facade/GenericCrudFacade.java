package facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dao.GenericDao;

@Stateless
public abstract class GenericCrudFacade<T> {

    private final static String UNIT_NAME = "itinerarioPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;
	
	protected EntityManager getEntityManager() {
		return em;
	}
	public T recuperar(Object id) throws Exception {
		return getDao().recuperar(id);
	}
	
	public T salvar(T entidade) throws Exception {
		return getDao().salvar(entidade);
	}

	public void excluir(T entidade) throws Exception {
		getDao().excluir(entidade);
	}
	
	public List<T> listar() throws Exception {
		return getDao().listar();
	}

	protected abstract GenericDao<T> getDao();
	
}
