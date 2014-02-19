package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Escola;
import modelo.Aluno;
import modelo.Pessoa;
import util.JsfUtil;
import facade.AlunoFacade;

@ManagedBean
@ViewScoped
public class AlunoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Aluno aluno;
	private List<Aluno> lista;
	private String estadoView;
	private Pessoa pessoaPesquisa;
	private Escola escolaPesquisa;
	@EJB
	private AlunoFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public void listar() { 
		try {
			if (pessoaPesquisa == null && escolaPesquisa == null) {
				this.lista = facade.listar();
			} else if (pessoaPesquisa != null && escolaPesquisa == null) {
				this.lista = facade.listar(pessoaPesquisa);
			} else if (pessoaPesquisa == null && escolaPesquisa != null) {
				this.lista = facade.listar(escolaPesquisa);
			} else if (pessoaPesquisa != null && escolaPesquisa != null) {
				this.lista = facade.listar(escolaPesquisa, pessoaPesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Aluno> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.aluno = new Aluno();
	}

	public void iniciarAlteracao(Aluno aluno) {
		this.aluno = aluno;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(aluno);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Aluno aluno) {
		this.aluno = aluno;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(aluno);
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

	public Pessoa getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(Pessoa pessoa) { 
		this.pessoaPesquisa = pessoa;
	}

	public Escola getEscolaPesquisa() {
		return escolaPesquisa;
	}

	public void setEscolaPesquisa(Escola veiculo) {
		this.escolaPesquisa = veiculo;
	}

}