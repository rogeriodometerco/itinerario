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
import motor.AnalisadorDeViagem;
import motor.AnaliseDePosicao;

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
	private Date dataInicial;
	private Date dataFinal;
	private List<AnalisadorDeViagem> analisadores;
	private AnalisadorDeViagem analisadorNoMapa;
	private String exibicao;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	private double[] total;

	@EJB
	private PosicaoVeiculoFacade facade;

	public static String EXIBICAO_TABELA = "TABELA";
	public static String EXIBICAO_MAPA = "MAPA";

	@PostConstruct
	private void inicializar() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 6);
		dataInicial = new Date(c.getTimeInMillis());
		dataFinal = new Date(c.getTimeInMillis());
		analisar();
		resetarMapa();
		this.exibicao = EXIBICAO_TABELA;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date data) {
		this.dataInicial = data;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date data) {
		this.dataInicial = data;
	}

	public List<AnalisadorDeViagem> getAnalisadores() {
		return analisadores;
	}

	public double[] getTotal() {
		return total;
	}

	public AnalisadorDeViagem getAnalisadorNoMapa() {
		return analisadorNoMapa;
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

	public void exibirMapa(AnalisadorDeViagem analisador) {
		this.analisadorNoMapa = analisador;
		this.exibicao = EXIBICAO_MAPA;
		this.mapModel = new DefaultMapModel();
		resetarMapa();
		criarMarcadores();
		criarLinhas();
	}

	private void criarLinhas() {
		Polyline polyline = new Polyline();
		for (PontoLinha ponto: analisadorNoMapa.getLinha().getPontos()) {
			LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(7);  
		polyline.setStrokeColor("#0000FF");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);  
		/*
                polyline = new Polyline();
                for (AnaliseDePosicao analise: analisadorNoMapa.getAnalises()) {
                        LatLng latLng = new LatLng(analise.getPosicaoVeiculo().getLat(), 
                                        analise.getPosicaoVeiculo().getLng());
                        polyline.getPaths().add(latLng);
                }
                polyline.setStrokeWeight(4);  
                polyline.setStrokeColor("#000000");  
                polyline.setStrokeOpacity(0.3);  
                mapModel.addOverlay(polyline);
		 */  
	}

	private void criarMarcadores() {
		/*
                for (PontoLinha ponto: analisadorNoMapa.getLinha().getPontos()) {
                        criarMarcador(ponto);
                }*/
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
		String legenda;
		if (analise.isNoTrajeto()) {
			if (analise.getPosicaoVeiculo().getVelocidade() > 0) {
				// TODO Ícone que representa veículo parado.
				icone = "mm_20_green.png";
				legenda = "No trajeto";
			} else {
				icone = "mm_20_white.png";
				legenda = "No trajeto, parado";
			}
		} else {
			icone = "mm_20_red.png";
			legenda = "Fora do trajeto";
		}
		LatLng latLng = new LatLng(analise.getPosicaoVeiculo().getLat(), 
				analise.getPosicaoVeiculo().getLng());
		Marker marker = new Marker(latLng, "", analise);
		marker.setIcon("resources/icones/" + icone);
		String titulo = marker.getLatlng().toString()
				+ "\n" + legenda
				+ "\nVeículo: "
				+ analise.getPosicaoVeiculo().getVeiculo().getIdentificacao()
				+ "\nData e hora: " 
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(analise.getPosicaoVeiculo().getDataHora())
		+ "\nVelocidade: " 
		+ analise.getPosicaoVeiculo().getVelocidade() + " km/h";
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}


	private void resetarMapa() {
		if (analisadorNoMapa == null) {
			this.centroMapa = "-24.753573,-51.762526";
		} else {
			this.centroMapa = 
					analisadorNoMapa.getLinha().getPontos().get(0).getLat() 
					+ ", " + analisadorNoMapa.getLinha().getPontos().get(0).getLng();
		} 
		//this.mapModel = new DefaultMapModel();
		this.zoomMapa = 13;
	}

	public String getExibicao() {
		return exibicao;
	}

	public void analisar() {
		try {
			this.analisadores = facade.analisarPosicoesDeCadaProgramacao(dataInicial, dataFinal);
			totalizar();
			exibirTabela();
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}

	private void totalizar() {
		total = new double[4];
		for (int i=0; i <= 3; i++) {
			total[i] = 0;
		}
		for (AnalisadorDeViagem analisador: analisadores) {
			total[0] += analisador.getProgramacao().getLinha().getQuilometragem();
			total[1] += analisador.getDistanciaNoTrajeto();
			total[2] += analisador.getDiferencaDeDistancia();
			total[3] += analisador.getDistanciaForaDoTrajeto();
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