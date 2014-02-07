package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Calendario;
import util.JsfUtil;
import facade.CalendarioFacade;

@ManagedBean
@ViewScoped
public class CalendarioMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Calendario calendario;
	private List<Calendario> lista;
	private String estadoView;
	private String nomePesquisa;

	@EJB
	private CalendarioFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}

	public Calendario getcalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public void listar() { 
		try {
			if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {
				this.lista = facade.listar();
			} else {
				this.lista = facade.listarPorNomeContendo(nomePesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Calendario> autocomplete(String chave) {
		try {
			return facade.listarPorNomeContendo(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para calendario: " + e.getMessage());
		}
		return null;
	}

	public List<Calendario> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.calendario = new Calendario();
	}

	public void iniciarAlteracao(Calendario calendario) {
		this.calendario = calendario;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(calendario);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Calendario calendario) {
		this.calendario = calendario;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(calendario);
			JsfUtil.addMsgSucesso("Informações excluídas com sucesso.");
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

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

}