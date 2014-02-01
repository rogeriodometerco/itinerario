package managedbean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Veiculo;
import util.JsfUtil;
import facade.GenericCrudFacade;
import facade.VeiculoFacade;

public abstract class GenericCrudMb<T>  {
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private T entidade;
	private List<T> lista;
	private String estadoView;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		System.out.println("GenericCrudMb.inicializar()");
	}

	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(T entidade) {
		this.entidade = entidade;
	}
	
	public List<T> getLista() {
		if (lista == null) {
			listar();
		}
		return lista;
	}

	public void listar() { 
		try {
			this.lista = recuperarLista();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public void iniciarCriacao() {
		try {
			this.estadoView = CRIACAO;
			this.entidade = getClasseEntidade().newInstance();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao instanciar entidade: " + e.getMessage());
		}
	}

	public void iniciarAlteracao(T entidade) {
		this.entidade = entidade;
		this.estadoView = ALTERACAO;
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getClasseEntidade() {
		return (Class<T>)((ParameterizedType)
				getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}


	public void salvar() {
		try {
			getServico().salvar(entidade);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			lista = null;
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(T entidade) {
		this.entidade = entidade;
		this.estadoView = EXCLUSAO;
	}

	public void confirmarExclusao() {
		try {
			getServico().excluir(entidade);
			JsfUtil.addMsgSucesso("Registro excluído com sucesso.");
			lista = null;
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao excluir: " + e.getMessage());
		}
	}

	public void cancelar() {
		lista = null;
		this.estadoView = LISTAGEM;
	}

	public Boolean getExibirListagem() {
		return true;
		//return isListagem();
	}
	
	public Boolean getExibirEdicao() {
		return isCriacao() || isAlteracao() || isExclusao();
	}
	
	public Boolean isListagem() {
		return this.estadoView != null && this.estadoView.equals(LISTAGEM);
	}

	public Boolean isCriacao() {
		return this.estadoView != null && this.estadoView.equals(CRIACAO);
	}

	public Boolean isAlteracao() {
		return this.estadoView != null && this.estadoView.equals(ALTERACAO);
	}

	public Boolean isExclusao() {
		return this.estadoView != null && this.estadoView.equals(EXCLUSAO);
	}
	
	protected abstract GenericCrudFacade<T> getServico();

	protected abstract List<T> recuperarLista() throws Exception;

}