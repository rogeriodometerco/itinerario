package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Passageiro;
import modelo.Pessoa;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import util.JsfUtil;
import util.Paginador;
import util.RotaMapModel;
import facade.PassageiroFacade;

@ManagedBean
@ViewScoped
public class PassageiroMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Passageiro passageiro;
	private List<Passageiro> lista;
	private String estadoView;
	private String chavePesquisa;
	private RotaMapModel rotaMapModel;
	@EJB
	private PassageiroFacade facade;
	private Paginador paginador;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
		this.rotaMapModel = new RotaMapModel();
	}

	public Passageiro getPassageiro() {
		return passageiro;
	}

	public void setPassageiro(Passageiro passageiro) {
		this.passageiro = passageiro;
	}

	public List<Passageiro> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugest�es para passageiro: " + e.getMessage());
		}
		return null;
	}
	
	public void listar() { 
		try {
			if (chavePesquisa == null) {
				this.lista = facade.listar(paginador);
			} else {
				this.lista = facade.autocomplete(chavePesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Passageiro> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.passageiro = new Passageiro();
		this.passageiro.setPessoa(new Pessoa());
		sincronizarMapModel(true);
	}

	public void iniciarAlteracao(Passageiro passageiro) {
		this.passageiro = passageiro;
		this.estadoView = ALTERACAO;
		sincronizarMapModel(true);
		rotaMapModel.centralizarPeloPassageiro();
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(passageiro);
			JsfUtil.addMsgSucesso("Informa��es salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Passageiro passageiro) {
		this.passageiro = passageiro;
		this.estadoView = EXCLUSAO;
		sincronizarMapModel(true);
		rotaMapModel.centralizarPeloPassageiro();
	}

	public void terminarExclusao() {
		try {
			facade.excluir(passageiro);
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

	public Boolean temPaginaAnterior() {
		return paginador.getPaginaAtual() > 1;
	}

	public Boolean temProximaPagina() {
		if (lista == null) {
			return false;
		} else {
			return paginador.getTamanhoPagina() <= lista.size();
		}
	}

	public void paginaAnterior() {
		paginador.anterior();
		listar();
	}

	public void proximaPagina() {
		paginador.proxima();
		listar();
	}

	public String getChavePesquisa() {
		return chavePesquisa;
	}

	public void setChavePesquisa(String chave) { 
		this.chavePesquisa = chave;
	}

	public void onMapStateChange(StateChangeEvent event) {
		rotaMapModel.onMapStateChange(event);
	}

    public void onMarkerDrag(MarkerDragEvent event) {  
        passageiro.getPessoa().setLat(event.getMarker().getLatlng().getLat());  
        passageiro.getPessoa().setLng(event.getMarker().getLatlng().getLng());
		sincronizarMapModel(false);
    } 

	public void onPointSelect(PointSelectEvent event) {
		passageiro.getPessoa().setLat(event.getLatLng().getLat());
		passageiro.getPessoa().setLng(event.getLatLng().getLng());
		sincronizarMapModel(false);
	}

    public MapModel getMapModel() {
		return rotaMapModel.getMapModel();
	}

	public String getCentroMapa() {
		return rotaMapModel.getCentro();
	}

	public Integer getZoomMapa() {
		return rotaMapModel.getZoom();
	}

	private void sincronizarMapModel(boolean resetar) {
		if (resetar) {
			rotaMapModel.inicializarPropriedadesDeMapa();
		}
		rotaMapModel.setPassageiro(passageiro);
	}
}