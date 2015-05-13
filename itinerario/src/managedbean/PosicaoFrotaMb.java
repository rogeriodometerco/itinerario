package managedbean;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import util.JsfUtil;
import util.RotaMapModel;

import facade.PosicaoVeiculoFacade;

import modelo.PosicaoVeiculo;
import motor.AnalisadorPosicao;

@ManagedBean
@ViewScoped
public class PosicaoFrotaMb {
	private List<PosicaoVeiculo> posicoes;
	private PosicaoVeiculo posicaoEmFoco;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;

	@EJB
	private PosicaoVeiculoFacade facade;

	public List<PosicaoVeiculo> getPosicoes() {
		if (posicoes == null) {
			listar();
		}
		return posicoes;
	}

	public void listar() {
		try {
			posicoes = facade.recuperarUltimaPosicaoDeCadaVeiculo();
			this.posicaoEmFoco = null;
			mostrarPosicoesNoMapa();
			resetarMapa();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar última posição da frota: " + e.getMessage());
			posicoes = null;
		}
	}

	private void mostrarPosicoesNoMapa() {
		getMapModel().getMarkers().clear();
		for (PosicaoVeiculo posicao: posicoes) {
			criarMarcador(posicao);
		}
	}

	private void criarMarcador(PosicaoVeiculo posicao) {
		String icone = "resources/icones/mm_20_green.png";
		icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?"
				+ "text=" + posicao.getVeiculo().getPlaca()
				+ "&color=" + RotaMapModel.COR_MARCADOR_VEICULO;
		LatLng latLng = new LatLng(posicao.getLat(), posicao.getLng());
		Marker marker = new Marker(latLng, "", posicao);
		marker.setIcon(icone);
		String titulo = marker.getLatlng().toString()
				+ "\nVeículo: "
				+ posicao.getVeiculo().getPlaca()
				+ "\nData e hora: " 
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(posicao.getDataHora())
		+ "\nVelocidade: " 
		+ posicao.getVelocidade() + " km/h";
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}

	public void focar(PosicaoVeiculo posicao) {
		this.posicaoEmFoco = posicao;
		resetarMapa();
	}

	private void resetarMapa() {
		if (posicaoEmFoco == null) {
			this.centroMapa = RotaMapModel.CENTRO_MAPA_PADRAO;
			this.zoomMapa = RotaMapModel.ZOOM_MAPA_PADRAO;
		} else {
			this.centroMapa = 
					posicaoEmFoco.getLat() 
					+ ", " + posicaoEmFoco.getLng();
			this.zoomMapa = 15;
		} 
		/*
		String icone = "";
		for (Marker m: getMapModel().getMarkers()) {
			if (posicaoEmFoco != null && ((PosicaoVeiculo)m.getData()).equals(posicaoEmFoco)) {
				icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?"
						+ "text=" + ((PosicaoVeiculo)m.getData()).getVeiculo().getPlaca()
						+ "&color=" + RotaMapModel.COR_MARCADOR_VEICULO;

			} else {
				icone = "resources/icones/mm_20_green.png";
			}
			m.setIcon(icone);
		}
		*/
	}


	public void onStateChange(StateChangeEvent event) {
		zoomMapa = event.getZoomLevel();
		centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

	public MapModel getMapModel() {
		if (mapModel == null) {
			mapModel = new DefaultMapModel();
		}
		return mapModel;
	}

	public String getCentroMapa() {
		return centroMapa;
	}

	public Integer getZoomMapa() {
		return zoomMapa;
	}


}