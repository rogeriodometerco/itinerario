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
import facade.PassageiroFacade;

@ManagedBean
@ViewScoped
public class PassageiroMb_bkp implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Passageiro passageiro;
	private List<Passageiro> lista;
	private String estadoView;
	private String chavePesquisa;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	@EJB
	private PassageiroFacade facade;
	private Paginador paginador;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10); 
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
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para passageiro: " + e.getMessage());
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
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void iniciarAlteracao(Passageiro passageiro) {
		this.passageiro = passageiro;
		this.estadoView = ALTERACAO;
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(passageiro);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Passageiro passageiro) {
		this.passageiro = passageiro;
		this.estadoView = EXCLUSAO;
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void terminarExclusao() {
		try {
			facade.excluir(passageiro);
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
		zoomMapa = event.getZoomLevel();
		centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

    public void onMarkerDrag(MarkerDragEvent event) {  
        passageiro.getPessoa().setLat(event.getMarker().getLatlng().getLat());  
        passageiro.getPessoa().setLng(event.getMarker().getLatlng().getLng());
        mapModel.getMarkers().get(0).setLatlng(event.getMarker().getLatlng());
		//sincronizarMapModel();
    } 

	public void onPointSelect(PointSelectEvent event) {
		//if (pessoa.getLat() == null && pessoa.getLng() == null) {
		passageiro.getPessoa().setLat(event.getLatLng().getLat());
		passageiro.getPessoa().setLng(event.getLatLng().getLng());
			if (mapModel.getMarkers() != null && mapModel.getMarkers().size() > 0) {
				mapModel.getMarkers().get(0).setLatlng(event.getLatLng());
			} else {
				Marker m = new Marker(new LatLng(passageiro.getPessoa().getLat(), passageiro.getPessoa().getLng()));
				//m.setDraggable(true);
				m.setTitle(m.getLatlng().toString());
				m.setIcon("resources/icones/male-2.png");
				mapModel.addOverlay(m);
			}
			//sincronizarMapModel();
		//}
	}

    public MapModel getMapModel() {
		return mapModel;
	}

	public String getCentroMapa() {
		return centroMapa;
	}

	public Integer getZoomMapa() {
		return zoomMapa;
	}

	private void sincronizarMapModel() {
		mapModel = new DefaultMapModel();
		Double lat = -24.754737;
		Double lng = -51.764410;
		if (passageiro.getPessoa() != null && passageiro.getPessoa().getLat() != null 
				&& passageiro.getPessoa().getLng() != null) {
			lat = passageiro.getPessoa().getLat();
			lng = passageiro.getPessoa().getLng();
			Marker m = new Marker(new LatLng(lat, lng));
			m.setTitle(m.getLatlng().toString());
			m.setIcon("resources/icones/male-2.png");
			//m.setDraggable(true);
			mapModel.addOverlay(m);  
		} 
		this.centroMapa = lat + ", " + lng;
		if (zoomMapa == null) {
			this.zoomMapa = 14;
		}
	}
}