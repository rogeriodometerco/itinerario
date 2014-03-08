package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Veiculo;
import util.JsfUtil;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class VeiculoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Veiculo veiculo;
	private List<Veiculo> lista;
	private String placaPesquisa;
	private String estadoView;
	@EJB
	private VeiculoFacade facade;
	
	//@ManagedProperty(value="#{rotaMb}")
	//private RotaMb rotaMb;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}
	
/*	public RotaMb getRotaMb() {
		return rotaMb;
	}
	
	public void setRotaMb(RotaMb r) {
		this.rotaMb = r;
	}

	public String teste() {
		JsfUtil.addMsgSucesso("A rota selecionada é: " + rotaMb.getRota().getCodigo());
		JsfUtil.addMsgSucesso("Quantidade de rotas listadas: " + rotaMb.getLista().size());
		rotaMb.getLista().get(0).setNome("rogerio fernando dometerco");
		//return "veiculo";
		return null;
	}
	*/
	public Veiculo getveiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public void listar() { 
		try {
			if (placaPesquisa == null || placaPesquisa.trim().length() == 0) {
				this.lista = facade.listar();
			} else {
				this.lista = facade.listarPorParteDaPlaca(placaPesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Veiculo> autocomplete(String parteDaPlaca) {
		try {
			return facade.listarPorParteDaPlaca(parteDaPlaca);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para veículo: " + e.getMessage());
		}
		return null;
	}
	
	public List<Veiculo> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.veiculo = new Veiculo();
	}

	public void iniciarAlteracao(Veiculo veiculo) {
		this.veiculo = veiculo;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(veiculo);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Veiculo veiculo) {
		this.veiculo = veiculo;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(veiculo);
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

	public String getPlacaPesquisa() {
		return placaPesquisa;
	}

	public void setPlacaPesquisa(String placaPesquisa) {
		this.placaPesquisa = placaPesquisa;
	}

}