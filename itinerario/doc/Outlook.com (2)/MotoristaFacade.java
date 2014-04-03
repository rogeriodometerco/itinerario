package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Motorista;
import modelo.Escola;
import modelo.Pessoa;
import modelo.Rota;
import dao.MotoristaDao;

@Stateless
public class MotoristaFacade extends GenericCrudFacade<Motorista> {

	@EJB
	private MotoristaDao motoristaDao;

	@Override
	protected MotoristaDao getDao() {
		return motoristaDao;
	}

	public Motorista recuperar(Pessoa pessoa) throws Exception {
		String sql = "select x"
				+ " from Motorista as x" 
				+ " where x.pessoa = :pessoa";
		try {
			return getEntityManager()
					.createQuery(sql, Motorista.class)
					.setParameter("pessoa", pessoa)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Motorista> autocomplete(String chave) 
			throws Exception {
		String sql = "select x"
				+ " from Motorista x"
				+ " where (upper(x.codigo) like :chave"
				+ " or upper(x.pessoa.nome) like :chave)" 
				+ " or upper(x.pessoa.cpf) like :chave)"; 
		return getEntityManager()
				.createQuery(sql, Motorista.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.getResultList();
	}


	public List<Motorista> autocomplete(String chave, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Motorista x"
				+ " where (upper(x.codigo) like :chave"
				+ " or upper(x.pessoa.nome) like :chave)" 
				+ " or upper(x.pessoa.cpf) like :chave)"; 
		return getEntityManager()
				.createQuery(sql, Motorista.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	@Override
	protected void validar(Motorista entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getPessoa().getNome() == null || entidade.getPessoa().getNome().trim().length() == 0) {
			erros.add("Informe o nome da pessoa");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void validarExclusao(Motorista entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		// TODO Não permitir excluir se existe vínculo com veículo.
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}