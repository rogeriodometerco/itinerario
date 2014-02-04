package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import modelo.Pessoa;
import dao.PessoaDao;

@Stateless
public class PessoaFacade extends GenericCrudFacade<Pessoa> {

	@EJB
	private PessoaDao pessoaDao;
	
	@Override
	protected PessoaDao getDao() {
		return pessoaDao;
	}

	public List<Pessoa> listarPorNomeContendo(String parteDoNome) 
			throws Exception {
		TypedQuery<Pessoa> query = getEntityManager().createQuery(
				"select x from Pessoa x where " +
						"upper(x.nome) like :chave", Pessoa.class);
		query.setParameter("chave", "%" + parteDoNome.toUpperCase() + "%");
		return query.getResultList();
	}

	
}
