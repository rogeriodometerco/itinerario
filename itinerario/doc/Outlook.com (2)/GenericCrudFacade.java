package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dao.GenericDao;

@Stateless
public abstract class GenericCrudFacade<T> {

    private static final String UNIT_NAME = "itinerarioPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}
	public T recuperar(Object id) throws Exception {
		return getDao().recuperar(id);
	}

	//TODO remover este método.
	public T salvar(T entidade) throws Exception {
		completarEdicao(entidade);
		validar(entidade);
		return getDao().salvar(entidade);
	}

	public void excluir(T entidade) throws Exception {
		validarExclusao(entidade);
		getDao().excluir(entidade);
	}

	public List<T> listar() throws Exception {
		return getDao().listar();
	}

	public List<T> listar(Paginador paginador) throws Exception {
		return getDao().listar(paginador);
	}

	public List<T> salvar(List<T> lista) throws Exception {
		List<T> retorno = new ArrayList<T>();
		for (T entidade: lista) {
			completarEdicao(entidade);
			validar(entidade);
		}
		for (T entidade: lista) {
			retorno.add(getDao().salvar(entidade));
		}
		return retorno;
	}

	protected void validar(T entidade) throws Exception {
		//implementar na classe especialista
		//TODO pensar numa solução melhor
	}

	protected void validarExclusao(T entidade) throws Exception {
		//implementar na classe especialista
		//TODO pensar numa solução melhor
	}

	protected void completarEdicao(T entidade) throws Exception {
		//implementar na classe especialista
		//TODO pensar numa solução melhor
	}

	protected abstract GenericDao<T> getDao();

}