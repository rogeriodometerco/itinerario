package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Atendido;
import modelo.Pessoa;
import modelo.ProgramacaoRota;
import util.JsfUtil;
import facade.AtendidoFacade;

@ManagedBean
@ViewScoped
public class AtendidoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Atendido atendido;
	private List<Atendido> lista;
	private String estadoView;
	private ProgramacaoRota programacaoRotaPesquisa;
	private Pessoa pessoaPesquisa;
	@EJB
	private AtendidoFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}

	public Atendido getAtendido() {
		return atendido;
	}

	public void setAtendido(Atendido atendido) {
		this.atendido = atendido;
	}

	public void listar() { 
		try {
			if (programacaoRotaPesquisa == null && pessoaPesquisa == null) {
				this.lista = facade.listar();
			} else if (programacaoRotaPesquisa != null && pessoaPesquisa == null) {
				this.lista = facade.listar(programacaoRotaPesquisa);
			} else if (programacaoRotaPesquisa == null && pessoaPesquisa != null) {
				this.lista = facade.listar(pessoaPesquisa);
			} else if (programacaoRotaPesquisa != null && pessoaPesquisa != null) {
				this.lista = facade.listar(programacaoRotaPesquisa, pessoaPesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Atendido> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.atendido = new Atendido();
	}

	public void iniciarAlteracao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(atendido);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(atendido);
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

	public ProgramacaoRota getProgramacaoRotaPesquisa() {
		return programacaoRotaPesquisa;
	}

	public void setProgramacaoRotaPesquisa(ProgramacaoRota programacaoRota) {
		this.programacaoRotaPesquisa = programacaoRota;
	}

	public Pessoa getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(Pessoa pessoa) {
		this.pessoaPesquisa = pessoa;
	}
}