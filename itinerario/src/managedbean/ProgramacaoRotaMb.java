package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.ProgramacaoRota;
import modelo.Rota;
import modelo.Veiculo;
import util.JsfUtil;
import facade.ProgramacaoRotaFacade;

@ManagedBean
@ViewScoped
public class ProgramacaoRotaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private ProgramacaoRota programacaoRota;
	private List<ProgramacaoRota> lista;
	private String estadoView;
	private Rota rotaPesquisa;
	private Veiculo veiculoPesquisa;
	@EJB
	private ProgramacaoRotaFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}

	public ProgramacaoRota getProgramacaoRota() {
		return programacaoRota;
	}

	public void setProgramacaoRota(ProgramacaoRota programacaoRota) {
		this.programacaoRota = programacaoRota;
	}

	public void listar() { 
		try {
			if (rotaPesquisa == null && veiculoPesquisa == null) {
				this.lista = facade.listar();
			} else if (rotaPesquisa != null && veiculoPesquisa == null) {
				this.lista = facade.listar(rotaPesquisa);
			} else if (rotaPesquisa == null && veiculoPesquisa != null) {
				this.lista = facade.listar(veiculoPesquisa);
			} else if (rotaPesquisa != null && veiculoPesquisa != null) {
				this.lista = facade.listar(rotaPesquisa, veiculoPesquisa);
			}
			System.out.println(lista.size() + " programações listadas");
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<ProgramacaoRota> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.programacaoRota = new ProgramacaoRota();
	}

	public void iniciarAlteracao(ProgramacaoRota programacaoRota) {
		this.programacaoRota = programacaoRota;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(programacaoRota);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(ProgramacaoRota programacaoRota) {
		this.programacaoRota = programacaoRota;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(programacaoRota);
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

	public Rota getRotaPesquisa() {
		return rotaPesquisa;
	}

	public void setRotaPesquisa(Rota rota) {
		this.rotaPesquisa = rota;
	}

	public Veiculo getVeiculoPesquisa() {
		return veiculoPesquisa;
	}

	public void setVeiculoPesquisa(Veiculo veiculo) {
		this.veiculoPesquisa = veiculo;
	}

	public List<ProgramacaoRota> autocomplete(String chave) {
		try {
			return facade.listarAutocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para programação de veículo para rota: " + e.getMessage());
		}
		return null;
	}
}