package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.Passageiro;
import modelo.Pessoa;
import dao.PassageiroDao;

@Stateless
public class PassageiroFacade extends GenericCrudFacade<Passageiro> {

	@EJB
	private PassageiroDao passageiroDao;
	@EJB
	private PessoaFacade pessoaFacade;

	@Override
	protected PassageiroDao getDao() {
		return passageiroDao;
	}

	public Passageiro recuperar(Pessoa pessoa) throws Exception {
		String sql = "select x"
				+ " from Passageiro as x" 
				+ " where x.pessoa = :pessoa";
		try {
			return getEntityManager()
					.createQuery(sql, Passageiro.class)
					.setParameter("pessoa", pessoa)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Passageiro> autocomplete(String chave) 
			throws Exception {
		String sql = "select x"
				+ " from Passageiro x"
				+ " where (upper(x.codigo) like :chave"
				+ " or upper(x.pessoa.nome) like :chave)" 
				+ " or upper(x.pessoa.cpf) like :chave)"; 
		return getEntityManager()
				.createQuery(sql, Passageiro.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.getResultList();
	}

	@Override
	protected void validar(Passageiro entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getPessoa().getNome() == null || entidade.getPessoa().getNome().trim().length() == 0) {
			erros.add("Informe o nome da pessoa");
		}
		/*
		if (entidade.getId() == null && entidade.getPessoa() != null) {
			Passageiro m = recuperar(entidade.getPessoa());
			if (m.getPessoa().getId().equals(entidade.getPessoa().getId())) {
				erros.add("Pessoa já cadastrada como passageiro");
			}
		}
		*/
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void validarExclusao(Passageiro entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		// TODO Ver o que precisa validar.
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}
