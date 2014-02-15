package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Pessoa;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import util.JsfUtil;
import facade.PessoaFacade;

@ManagedBean
@ViewScoped
public class PessoaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Pessoa pessoa;
	private List<Pessoa> lista;
	private String estadoView;
	private String chavePesquisa;
	@EJB
	private PessoaFacade facade;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
	}
	
	public Pessoa getpessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public List<Pessoa> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para pessoa: " + e.getMessage());
		}
		return null;
	}
	
	public List<Pessoa> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.pessoa = new Pessoa();
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void iniciarAlteracao(Pessoa pessoa) {
		this.pessoa = pessoa;
		this.estadoView = ALTERACAO;
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(pessoa);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Pessoa pessoa) {
		this.pessoa = pessoa;
		this.estadoView = EXCLUSAO;
		this.zoomMapa = null;
		sincronizarMapModel();
	}

	public void terminarExclusao() {
		try {
			facade.excluir(pessoa);
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

	public String getChavePesquisa() {
		return chavePesquisa;
	}

	public void setChavePesquisa(String chavePesquisa) {
		this.chavePesquisa = chavePesquisa;
	}

	public void onMapStateChange(StateChangeEvent event) {
		zoomMapa = event.getZoomLevel();
		centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

    public void onMarkerDrag(MarkerDragEvent event) {  
        pessoa.setLat(event.getMarker().getLatlng().getLat());  
        pessoa.setLng(event.getMarker().getLatlng().getLng());
        mapModel.getMarkers().get(0).setLatlng(event.getMarker().getLatlng());
		//sincronizarMapModel();
    } 

	public void onPointSelect(PointSelectEvent event) {
		//if (pessoa.getLat() == null && pessoa.getLng() == null) {
			pessoa.setLat(event.getLatLng().getLat());
			pessoa.setLng(event.getLatLng().getLng());
			if (mapModel.getMarkers() != null && mapModel.getMarkers().size() > 0) {
				mapModel.getMarkers().get(0).setLatlng(event.getLatLng());
			} else {
				Marker m = new Marker(new LatLng(pessoa.getLat(), pessoa.getLng()));
				//m.setDraggable(true);
				m.setTitle(m.getLatlng().toString());
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
		Double lat = -24.750573;
		Double lng = -51.781526;
		if (pessoa != null && pessoa.getLat() != null && pessoa.getLng() != null) {
			lat = pessoa.getLat();
			lng = pessoa.getLng();
			Marker m = new Marker(new LatLng(lat, lng));
			m.setTitle(m.getLatlng().toString());
			//m.setDraggable(true);
			mapModel.addOverlay(m);  
		} 
		this.centroMapa = lat + ", " + lng;
		if (zoomMapa == null) {
			this.zoomMapa = 15;
		}
	}
}