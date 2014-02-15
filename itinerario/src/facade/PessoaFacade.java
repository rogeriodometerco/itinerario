package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
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

	public List<Pessoa> autocomplete(String chave) 
			throws Exception {
		TypedQuery<Pessoa> query = getEntityManager().createQuery(
				"select x from Pessoa x where" +
						" (upper(x.codigo) like :chave" +
						" or upper(x.nome) like :chave" +
						" or upper(x.cpf) like :chave)", Pessoa.class);
		query.setParameter("chave", "%" + chave.toUpperCase() + "%");
		return query.getResultList();
	}

	public Pessoa recuperarPorCodigo(String codigo) 
			throws Exception {
		String sql = "select x from Pessoa as x"
				+ " where upper(x.codigo) = :codigo";
		try {
			return (Pessoa)getEntityManager().createQuery(sql)
					.setParameter("codigo", codigo.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	public Pessoa recuperarPorCpf(String cpf) 
			throws Exception {
		String sql = "select x from Pessoa as x"
				+ " where x.cpf = :cpf";
		try {
			return (Pessoa)getEntityManager().createQuery(sql)
					.setParameter("cpf", cpf)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	@Override
	protected void validar(Pessoa entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getCodigo() == null || entidade.getCodigo().trim().equals("")) {
			erros.add("Informe o c�digo");
		}
		Pessoa p = recuperarPorCodigo(entidade.getCodigo());
		if (p != null && p.getCodigo().toUpperCase().equals(entidade.getCodigo().toUpperCase()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(p.getId()))) {
				erros.add("J� existe uma pessoa com o c�digo " + entidade.getCodigo());
			}
		}
		p = recuperarPorCpf(entidade.getCpf());
		if (p != null && p.getCpf().equals(entidade.getCpf()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(p.getId()))) {
				erros.add("J� existe uma pessoa com o CPF " + entidade.getCpf());
			}
		}
		if (entidade.getNome() == null || entidade.getNome().trim().equals("")) {
			erros.add("Informe o nome da pessoa");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void validarExclusao(Pessoa entidade) throws Exception {
		/*
		List<String> erros = new ArrayList<String>();
		List<PessoaRota> pessoas = pessoaRotaFacade.listar(entidade);
		if (pessoas != null && pessoas.size() > 0) {
			erros.add("N�o � poss�vel excluir a pessoa porque ela est� vinculada a uma ou mais rotas");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
		*/
	}

}
