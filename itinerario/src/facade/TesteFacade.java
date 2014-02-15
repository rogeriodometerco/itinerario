package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import managedbean.AppException;
import modelo.Escola;
import dao.EscolaDao;

@Stateless
public class TesteFacade extends GenericCrudFacade<Escola> {

	@EJB
	private EscolaDao escolaDao;
	
	@Override
	protected EscolaDao getDao() {
		return escolaDao;
	}

	@Override
	public List<Escola> salvar(List<Escola> lista) throws Exception {
		List<Escola> retorno = new ArrayList<Escola>();
		for (Escola entidade: lista) {
			retorno.add(getDao().salvar(entidade));
		}
		System.out.println("Foucei um erro");
		throw new AppException("Forçando erro");
		//return retorno;
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
