package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Escola;
import util.JsfUtil;
import facade.EscolaFacade;

@ManagedBean
@ViewScoped
public class EscolaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Escola escola;
	private List<Escola> lista;
	private String estadoView;
	private String chavePesquisa;
	@EJB
	private EscolaFacade facade;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}
	
	public Escola getescola() {
		return escola;
	}

	public void setEscola(Escola escola) {
		this.escola = escola;
	}

	public void listar() { 
		try {
			if (chavePesquisa == null || chavePesquisa.trim().isEmpty()) {
				this.lista = facade.listar();
			} else {
				this.lista = facade.autocomplete(chavePesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Escola> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugest�es para escola: " + e.getMessage());
		}
		return null;
	}
	
	public List<Escola> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.escola = new Escola();
	}

	public void iniciarAlteracao(Escola escola) {
		this.escola = escola;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(escola);
			JsfUtil.addMsgSucesso("Informa��es salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Escola escola) {
		this.escola = escola;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(escola);
			JsfUtil.addMsgSucesso("Informa��es exclu�das com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao excluir: " + e.getMessage());
		}
	}

	public void cancelar() {
		listar();
		this.estadoView = LISTAGEM;
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

	public String getChavePesquisa() {
		return chavePesquisa;
	}

	public void setChavePesquisa(String chavePesquisa) {
		this.chavePesquisa = chavePesquisa;
	}
}