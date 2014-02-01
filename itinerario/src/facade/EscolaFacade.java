package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import modelo.Escola;
import modelo.Rota;
import dao.EscolaDao;
import dao.GenericDao;

@Stateless
public class EscolaFacade extends GenericCrudFacade<Escola> {

	@EJB
	private EscolaDao escolaDao;
	
	@Override
	protected GenericDao<Escola> getDao() {
		return escolaDao;
	}

	public List<Escola> listarPorNomeContendo(String parteDoNome) 
			throws Exception {
		TypedQuery<Escola> query = getEntityManager().createQuery(
				"select x from Escola x where " +
						"upper(x.nome) like :chave", Escola.class);
		query.setParameter("chave", "%" + parteDoNome.toUpperCase() + "%");
		return query.getResultList();
	}

	
}
