package managedbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.PontoLinha;
import motor.AnalisadorDePosicao;
import motor.AnaliseDePosicao;

import org.primefaces.context.RequestContext;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.PosicaoVeiculoFacade;

@ManagedBean
@ViewScoped
public class AnaliseMb {
	private Date data;
	private List<AnalisadorDePosicao> analisadores;
	private AnalisadorDePosicao analisadorNoMapa;
	private String exibicao;
	private Double diferencaTotalEmKm;
	private Double kmTotalForaDoTrajeto;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;

	@EJB
	private PosicaoVeiculoFacade facade;

	public static String EXIBICAO_TABELA = "TABELA";
	public static String EXIBICAO_MAPA = "MAPA";

	@PostConstruct
	private void inicializar() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 9, 25);
		data = new Date(c.getTimeInMillis());
		analisar();
		resetarMapa();
		this.exibicao = EXIBICAO_TABELA;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public List<AnalisadorDePosicao> getAnalisadores() {
		return analisadores;
	}

	public AnalisadorDePosicao getAnalisadorNoMapa() {
		return analisadorNoMapa;
	}

	public Double getDiferencaTotalEmKm() {
		return this.diferencaTotalEmKm;
	}

	public Double getKmTotalForaDoTrajeto() {
		return kmTotalForaDoTrajeto;
	}

	public void exibirTabela() {
		this.exibicao = EXIBICAO_TABELA;
		this.analisadorNoMapa = null;
	}

	public Boolean getExibicaoTabela() {
		return this.exibicao.equals(EXIBICAO_TABELA);
	}
	public Boolean getExibicaoMapa() {
		return this.exibicao.equals(EXIBICAO_MAPA);
	}

	public void exibirMapa(AnalisadorDePosicao analisador) {
		this.analisadorNoMapa = analisador;
		this.exibicao = EXIBICAO_MAPA;
		this.mapModel = new DefaultMapModel();
		resetarMapa();
		//criarMarcadores();
		criarLinhas();
	}

	private void criarLinhas() {
		Polyline polyline = new Polyline();
		for (PontoLinha ponto: analisadorNoMapa.getLinha().getPontos()) {
			LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(7);  
		polyline.setStrokeColor("#AAAAFF");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);  

		polyline = new Polyline();
		for (AnaliseDePosicao analise: analisadorNoMapa.getAnalises()) {
			LatLng latLng = new LatLng(analise.getPosicaoVeiculo().getLat(), 
					analise.getPosicaoVeiculo().getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(4);  
		polyline.setStrokeColor("#00FF00");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);  
	}

	private void criarMarcadores() {
		for (PontoLinha ponto: analisadorNoMapa.getLinha().getPontos()) {
			criarMarcador(ponto);
		}
		for (AnaliseDePosicao analise: analisadorNoMapa.getAnalises()) {
			criarMarcador(analise);
		}
		System.out.println(mapModel.getMarkers().size() + " marcadores no mapa");
	}

	private void criarMarcador(PontoLinha pontoLinha) {
		String icone = "mm_20_white.png";

		LatLng latLng = new LatLng(pontoLinha.getLat(), 
				pontoLinha.getLng());
		Marker marker = new Marker(latLng, "", pontoLinha);
		marker.setIcon("resources/icones/" + icone);
		String titulo = marker.getLatlng().toString()
				+ "\nLinha: "
				+ pontoLinha.getLinha().getNome()
				+ "\nSequencia: " 
				+ pontoLinha.getSequencia();
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);

	}

	private void criarMarcador(AnaliseDePosicao analise) {
		String icone;
		if (analise.isNoTrajeto()) {
			icone = "mm_20_green.png";
		} else {
			icone = "mm_20_red.png";
		}
		LatLng latLng = new LatLng(analise.getPosicaoVeiculo().getLat(), 
				analise.getPosicaoVeiculo().getLng());
		Marker marker = new Marker(latLng, "", analise);
		marker.setIcon("resources/icones/" + icone);
		String titulo = marker.getLatlng().toString()
				+ "\nVeículo: "
				+ analise.getPosicaoVeiculo().getVeiculo().getIdentificacao()
				+ "\nData e hora: " 
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm")
		.format(analise.getPosicaoVeiculo().getDataHora())
		+ "\nVelocidade: " 
		+ analise.getPosicaoVeiculo().getVelocidade() + " km/h";
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}


	private void resetarMapa() {
		if (analisadorNoMapa == null) {
			this.centroMapa = "-24.750573,-51.781526";
		} else {
			this.centroMapa = 
					analisadorNoMapa.getLinha().getPontos().get(0).getLat() 
					+ ", " + analisadorNoMapa.getLinha().getPontos().get(0).getLng();
		} 
		//this.mapModel = new DefaultMapModel();
		this.zoomMapa = 12;
	}

	public String getExibicao() {
		return exibicao;
	}

	public void analisar() {
		try {
			this.analisadores = facade.analisarPosicoesDeCadaEscala(data);
			totalizar();
			System.out.println("Ok. Data " + new SimpleDateFormat("dd/MM/yyyy").format(data));
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}

	private void totalizar() {
		diferencaTotalEmKm = 0d;
		kmTotalForaDoTrajeto = 0d;
		for (AnalisadorDePosicao analisador: analisadores) {
			diferencaTotalEmKm += analisador.getDiferencaDeDistancia();
			kmTotalForaDoTrajeto += analisador.getDistanciaForaDoTrajeto();
		}
	}

	public void onStateChange(StateChangeEvent event) {
		zoomMapa = event.getZoomLevel();
		centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
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


}