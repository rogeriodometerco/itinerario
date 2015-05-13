package facade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import modelo.ArquivoImagem;
import modelo.Motorista;
import modelo.Pessoa;
import util.JsfUtil;
import util.Paginador;
import dao.MotoristaDao;

@Stateless
public class MotoristaFacade extends GenericCrudFacade<Motorista> {

	@EJB
	private MotoristaDao motoristaDao;

	@Override
	protected MotoristaDao getDao() {
		return motoristaDao;
	}

	public Motorista recuperarParaEdicao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}

	public Motorista recuperarParaExclusao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}

	private Motorista recuperarParaEdicaoOuExclusao(Long id) throws Exception {
		//TODO Ver por que não busca a imagem.
		String sql = "select x"
				+ " from Motorista as x" 
				+ " join fetch x.pessoa p"
				+ " left join fetch p.imagens"
				+ " where x.id = :id";
		return getEntityManager()
				.createQuery(sql, Motorista.class)
				.setParameter("id", id)
				.getSingleResult();
	}

	/*
	public ArquivoImagem recuperarArquivoImagem(Long motoristaId) throws Exception {
		// TODO Refactoring
		Motorista m = this.recuperar(motoristaId);
		if (m.getPessoa().getImagens().size() > 0) {
			return m.getPessoa().getImagens().get(0);
		}
		return null;
	}
	*/
	
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