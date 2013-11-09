package dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class GenericDao<T> {
    private final static String UNIT_NAME = "itinerarioPU";

    @PersistenceContext(unitName = UNIT_NAME)
    private EntityManager em;
	
	private Class<T> classeEntidade;

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void inicializar() {
		this.classeEntidade = (Class<T>)((ParameterizedType)
				getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	public T recuperar(Object id) throws Exception {
		try {
			return (T)getEntityManager().find(classeEntidade, id);
		}
		catch (Exception e) {
			throw new Exception("Erro ao recuperar entidade " 
					+ classeEntidade.getSimpleName(), e);
		}
	}
	
	public T salvar(T entidade) throws Exception {
		try {
			this.getEntityManager().merge(entidade);
			return entidade;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao salvar entidade "
					+ classeEntidade.getSimpleName(), e);
		}
	}

	public void excluir(T entidade) throws Exception {
		try {
			this.getEntityManager().remove(getEntityManager().merge(entidade));
		}
		catch (Exception e) {
			throw new Exception("Erro ao remover entidade " 
					+ classeEntidade.getSimpleName(), e);
		}
	}
	
	public List<T> listar() throws Exception {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<T> cq = cb.createQuery(classeEntidade);
			Root<T> root = cq.from(classeEntidade);
			cq.select(root);

			TypedQuery<T> q = em.createQuery(cq);
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao listar entidades " 
					+ classeEntidade.getSimpleName(), e);
		}
	}

}
 