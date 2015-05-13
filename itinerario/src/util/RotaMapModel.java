package util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;

import modelo.AnaliseParada;
import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.Passageiro;
import modelo.PontoRota;
import modelo.PosicaoVeiculo;
import modelo.Rota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;


public class RotaMapModel {
	public static final String COR_MARCADOR_INICIO = "DF7401";
	public static final String COR_MARCADOR_TERMINO = "DF7401";
	public static final String COR_MARCADOR_PASSAGEM = "01A9DB"; //3CB371"; //5f9f9f";
	public static final String COR_MARCADOR_PARADA = "D7DF01"; //ff8c00";
	public static final String COR_MARCADOR_VEICULO = "01A9DB"; 
	public static final String CENTRO_MAPA_PADRAO = "-24.754737, -51.764410";
	public static final String COR_PASSAGEIRO = "2EFE2E";
	public static final String COR_PARADA_CUMPRIDA = "D7DF01";
	public static final String COR_PARADA_NAO_CUMPRIDA = "FF0000";
	public static final String COR_NO_TRAJETO = "01A9DB";
	public static final String COR_FORA_DO_TRAJETO = "FF0000";
	public static final int LARGURA_LINHA_ROTA = 7;
	public static final String COR_LINHA_ROTA = "0000FF";
	public static final double OPACIDADE_LINHA_ROTA = 0.2;
	public static final int LARGURA_LINHA_VIAGEM = 4;
	public static final String COR_LINHA_VIAGEM = "01A9DB";
	public static final double OPACIDADE_LINHA_VIAGEM = 0.5;
	public static final int ZOOM_MAPA_PADRAO = 14;
	private String centro;
	private int zoom;
	private MapModel mapModel;
	private boolean exibirPontoPassagem;
	private boolean exibirPontoParada;
	private boolean draggable;
	private Rota rota;
	private Passageiro passageiro;
	private AnaliseViagem analiseViagem;
	private List<PosicaoVeiculo> posicoesVeiculo;

	public RotaMapModel() {
		this.mapModel = new DefaultMapModel();
		this.exibirPontoPassagem = true;
		this.exibirPontoParada = true;
		this.draggable = true;
		inicializarPropriedadesDeMapa();
	}

	public void inicializarPropriedadesDeMapa() {
		this.zoom = ZOOM_MAPA_PADRAO;
		this.centro = CENTRO_MAPA_PADRAO;
	}

	public boolean exibePontoPassagem() {
		return exibirPontoPassagem;
	}

	public void exibirPontoPassagem(boolean exibirPontoPassagem) {
		this.exibirPontoPassagem = exibirPontoPassagem;
	}

	public boolean exibePontoParada() {
		return exibirPontoParada;
	}

	public void exibirPontoParada(boolean exibirPontoParada) {
		this.exibirPontoParada = exibirPontoParada;
	}

	public void naoExibirPontosDaRota() {
		this.exibirPontoParada = false;
		this.exibirPontoPassagem = false;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public void setCentro(double lat, double lng) {
		this.centro = String.valueOf(lat) + ", " + String.valueOf(lng);
	}

	public void centralizarPelaRota() {
		if (rota.getPontos() != null && rota.getPontos().size() > 0) {
			this.centro = rota.getPontos().get(0).getLat().toString() 
					+ ", " + rota.getPontos().get(0).getLng().toString(); 
		}
	}

	public void centralizarPeloPassageiro() {
		if (passageiro != null && passageiro.getPessoa().getLat() != null) {
			this.centro = passageiro.getPessoa().getLat().toString() 
					+ ", " + passageiro.getPessoa().getLng().toString(); 
		}
	}

	public void centralizarPelaAnaliseViagem() {
		if (analiseViagem != null) {
			if (analiseViagem.getAnalisesPosicao() != null && analiseViagem.getAnalisesPosicao().size() > 0) {
				this.centro = analiseViagem.getAnalisesPosicao().get(0).getPosicaoVeiculo().getLat().toString()
						+ ", " + analiseViagem.getAnalisesPosicao().get(0).getPosicaoVeiculo().getLng().toString();
			} else {
				centralizarPelaRota();
			}
		}
	}

	public void centralizarPelasPosicoesVeiculo() {
		if (posicoesVeiculo != null && posicoesVeiculo.size() > 0) {
			this.centro = posicoesVeiculo.get(0).getLat().toString() 
					+ ", " + posicoesVeiculo.get(0).getLng().toString(); 
		}
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	private void desenhar() {
		mapModel.getMarkers().clear();
		mapModel.getPolylines().clear();
		desenharRota();
		desenharPassageiro();
		desenharAnaliseViagem();
		desenharPosicoesVeiculo();
	}

	private void desenharRota() {
		if (rota != null && rota.getPontos().size() > 0) {
			Polyline polyline = new Polyline();
			for (PontoRota ponto: rota.getPontos()) {
				LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
				polyline.getPaths().add(latLng);
				if (ponto.getParada() && exibePontoParada() || !ponto.getParada() && exibePontoPassagem()) {
					criarMarcador(ponto);
				}
			}
			polyline.setStrokeWeight(LARGURA_LINHA_ROTA);  
			polyline.setStrokeColor(COR_LINHA_ROTA);  
			polyline.setStrokeOpacity(OPACIDADE_LINHA_ROTA);  
			mapModel.addOverlay(polyline);  
		}
	}

	private void desenharPassageiro() {
		if (passageiro != null && passageiro.getPessoa().getLat() != null) {
			LatLng latLng = new LatLng(passageiro.getPessoa().getLat(), 
					passageiro.getPessoa().getLng());
			Marker marker = new Marker(latLng, "", passageiro);
			String primeiroNome = "";
			if (passageiro.getPessoa().getNome() != "") {
				primeiroNome = new StringTokenizer(" " + passageiro.getPessoa().getNome()).nextToken(" ");
			}
			String icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?"
					+ "text=" + primeiroNome
					+ "&color=" + COR_PASSAGEIRO;
			marker.setIcon(icone);

			marker.setTitle(primeiroNome);
			marker.setData(passageiro);
			marker.setDraggable(draggable);
			this.mapModel.addOverlay(marker);
		}
	}

	private void desenharAnaliseViagem() {
		if (analiseViagem != null) {
			Polyline polyline = new Polyline();
			naoExibirPontosDaRota();
			desenharRota();
			for (AnalisePosicao analisePosicao: analiseViagem.getAnalisesPosicao()) {
				criarMarcador(analisePosicao);
				polyline.getPaths().add(new LatLng(analisePosicao.getPosicaoVeiculo().getLat(), 
						analisePosicao.getPosicaoVeiculo().getLng()));
			}
			polyline.setStrokeWeight(LARGURA_LINHA_VIAGEM);  
			polyline.setStrokeColor(COR_LINHA_VIAGEM);  
			polyline.setStrokeOpacity(OPACIDADE_LINHA_VIAGEM);  
			mapModel.addOverlay(polyline);
			for (AnaliseParada analiseParada: analiseViagem.getAnalisesParada()) {
				criarMarcador(analiseParada);				
			}
		}
	}

	private void desenharPosicoesVeiculo() {
		if (posicoesVeiculo != null) {
			Polyline polyline = new Polyline();
			for (PosicaoVeiculo posicaoVeiculo: posicoesVeiculo) {
				criarMarcador(posicaoVeiculo);
				polyline.getPaths().add(new LatLng(posicaoVeiculo.getLat(), posicaoVeiculo.getLng()));
			}
			polyline.setStrokeWeight(LARGURA_LINHA_VIAGEM);  
			polyline.setStrokeColor(COR_LINHA_VIAGEM);  
			polyline.setStrokeOpacity(OPACIDADE_LINHA_VIAGEM);  
			mapModel.addOverlay(polyline);
		}
	}

	private void criarMarcador(PontoRota pontoRota) {
		//http://thydzik.com/thydzikGoogleMap/markerlink.php?text=12&color=5680FC
		String cor = null;

		if (pontoRota.getSequencia() == 1) {
			cor = COR_MARCADOR_INICIO;
		} else if (pontoRota.getSequencia() == rota.getPontos().size()) {
			cor = COR_MARCADOR_TERMINO;
		} else if (pontoRota.getParada()) {
			cor = COR_MARCADOR_PARADA;
		} else {
			cor = COR_MARCADOR_PASSAGEM;
		} 

		String numero = "";
		if (pontoRota.getParada()) {
			numero = String.valueOf(pontoRota.getNumeroParada());
		} else {
			numero = String.valueOf(pontoRota.getSequencia());
		}
		String icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?text=" 
				+ numero
				+ "&color=" + cor;

		LatLng latLng = new LatLng(pontoRota.getLat(), 
				pontoRota.getLng());
		Marker marker = new Marker(latLng, "", pontoRota);
		marker.setIcon(icone);
		String titulo = null;

		if (pontoRota.getDescricao() != "" && pontoRota.getDescricao() != null) {
			titulo = pontoRota.getDescricao();
		} else {
			titulo = "Ponto " + pontoRota.getSequencia();
		}
		if (pontoRota.getSequencia() == 1) {
			titulo = titulo + "\n" + "Início do trajeto";
		} else if (pontoRota.getSequencia() == rota.getPontos().size()) {
			titulo = titulo + "\n" + "Término do trajeto";
		} else if (pontoRota.getParada()) {
			titulo = titulo + "\n" + "Ponto de parada";
		}
		marker.setTitle(titulo);
		marker.setData(pontoRota);
		marker.setDraggable(draggable);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcador(AnaliseParada analiseParada) {
		//http://thydzik.com/thydzikGoogleMap/markerlink.php?text=12&color=5680FC
		String cor = "";
		String titulo = "";

		if (analiseParada.getPontoParada().getDescricao() != "") {
			titulo = analiseParada.getPontoParada().getDescricao() + "\n";
		}

		if (analiseParada.getCumprida()) {
			cor = COR_PARADA_CUMPRIDA;
			titulo = titulo + "Parada cumprida";
		} else {
			cor = COR_PARADA_NAO_CUMPRIDA;
			titulo = titulo + "Parada não cumprida";
		} 

		String numero = String.valueOf(analiseParada.getPontoParada().getNumeroParada());
		String icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?text=" 
				+ numero
				+ "&color=" + cor;

		LatLng latLng = new LatLng(analiseParada.getPontoParada().getLat(), 
				analiseParada.getPontoParada().getLng());
		Marker marker = new Marker(latLng, "", analiseParada);
		marker.setIcon(icone);
		marker.setTitle(titulo);
		marker.setDraggable(draggable);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcador(AnalisePosicao analisePosicao) {
		String cor = "";
		String titulo = "";
		if (analisePosicao.getNoTrajeto()) {
			titulo = "No trajeto";
			cor = COR_NO_TRAJETO;
		} else {
			titulo = "Fora do trajeto";
			cor = COR_FORA_DO_TRAJETO;
		}
		titulo = titulo 
				+ "\n Em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(analisePosicao.getPosicaoVeiculo().getDataHora())
		+ "\n A " + analisePosicao.getPosicaoVeiculo().getVelocidade() + " km/h";

		String texto = new SimpleDateFormat("HH:mm").format(analisePosicao.getPosicaoVeiculo().getDataHora());
		String icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?text=" 
				+ texto
				+ "&color=" + cor;

		LatLng latLng = new LatLng(analisePosicao.getPosicaoVeiculo().getLat(), 
				analisePosicao.getPosicaoVeiculo().getLng());
		Marker marker = new Marker(latLng, "", analisePosicao);
		marker.setIcon(icone);
		marker.setTitle(titulo);
		marker.setDraggable(draggable);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcador(PosicaoVeiculo posicaoVeiculo) {
		String cor = COR_MARCADOR_VEICULO;
		String titulo = "Em " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(posicaoVeiculo.getDataHora())
		+ "\n A " + posicaoVeiculo.getVelocidade() + " km/h";

		String texto = new SimpleDateFormat("HH:mm").format(posicaoVeiculo.getDataHora());
		String icone = "http://thydzik.com/thydzikGoogleMap/markerlink.php?text=" 
				+ texto
				+ "&color=" + cor;

		LatLng latLng = new LatLng(posicaoVeiculo.getLat(), 
				posicaoVeiculo.getLng());
		Marker marker = new Marker(latLng, "", posicaoVeiculo);
		marker.setIcon(icone);
		marker.setTitle(titulo);
		marker.setDraggable(draggable);
		this.mapModel.addOverlay(marker);
	}

	public void onMapStateChange(StateChangeEvent event) {
		this.zoom = event.getZoomLevel();
		this.centro = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

	public void setRota(Rota rota) {
		this.rota = rota;
		desenhar();
	}

	public void setPassageiro(Passageiro passageiro) {
		this.passageiro = passageiro;
		desenhar();
	}

	public void setAnaliseViagem(AnaliseViagem analiseViagem) {
		this.analiseViagem = analiseViagem;
		desenhar();
	}

	public void setPosicoesVeiculo(List<PosicaoVeiculo> lista) {
		this.posicoesVeiculo = lista;
		desenhar();
	}
}
