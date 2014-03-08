package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Motorista;
import modelo.MotoristaVeiculo;
import modelo.Veiculo;
import util.JsfUtil;
import facade.MotoristaVeiculoFacade;

@ManagedBean
@ViewScoped
public class MotoristaVeiculoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private MotoristaVeiculo motoristaVeiculo;
	private List<MotoristaVeiculo> lista;
	private String estadoView;
	private Veiculo veiculoPesquisa;
	private Motorista motoristaPesquisa;
	@EJB
	private MotoristaVeiculoFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}

	public MotoristaVeiculo getMotoristaVeiculo() {
		return motoristaVeiculo;
	}

	public void setMotoristaVeiculo(MotoristaVeiculo motoristaVeiculo) {
		this.motoristaVeiculo = motoristaVeiculo;
	}

	public void listar() { 
		try {
			if (veiculoPesquisa == null && motoristaPesquisa == null) {
				this.lista = facade.listar();
			} else if (veiculoPesquisa != null && motoristaPesquisa == null) {
				this.lista = facade.listar(veiculoPesquisa);
			} else if (veiculoPesquisa == null && motoristaPesquisa != null) {
				this.lista = facade.listar(motoristaPesquisa);
			} else if (veiculoPesquisa != null && motoristaPesquisa != null) {
				this.lista = facade.listar(motoristaPesquisa, veiculoPesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<MotoristaVeiculo> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.motoristaVeiculo = new MotoristaVeiculo();
	}

	public void iniciarAlteracao(MotoristaVeiculo motoristaVeiculo) {
		this.motoristaVeiculo = motoristaVeiculo;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(motoristaVeiculo);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(MotoristaVeiculo motoristaVeiculo) {
		this.motoristaVeiculo = motoristaVeiculo;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(motoristaVeiculo);
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

	public Veiculo getVeiculoPesquisa() {
		return veiculoPesquisa;
	}

	public void setVeiculoPesquisa(Veiculo veiculo) {
		this.veiculoPesquisa = veiculo;
	}

	public Motorista getMotoristaPesquisa() {
		return motoristaPesquisa;
	}

	public void setMotoristaPesquisa(Motorista motorista) {
		this.motoristaPesquisa = motorista;
	}

}