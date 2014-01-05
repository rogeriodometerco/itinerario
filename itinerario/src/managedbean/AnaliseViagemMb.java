package managedbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.PontoLinha;
import modelo.PontoRota;
import modelo.Veiculo;
import motor.AnalisadorDeViagemAntigo;
import motor.AnaliseDePosicaoAntigo;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.AnalisadorFacade;
import facade.PosicaoVeiculoFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class AnaliseViagemMb {
	private Date dataInicial;
	private Date dataFinal;
	private Veiculo veiculo;
	private String exibicao;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	private double[] total;

	private List<AnaliseViagem> analises;
	private AnaliseViagem analiseNoMapa;
	
	@EJB
	private VeiculoFacade veiculoFacade;
	@EJB
	private AnalisadorFacade analisadorFacade;

	public static String EXIBICAO_TABELA = "TABELA";
	public static String EXIBICAO_MAPA = "MAPA";

	@PostConstruct
	private void inicializar() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 25);
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
		this.dataFinal = data;
	}

	public void identificacaoVeiculoChange() {
		try {
			if (getVeiculo().getIdentificacao() != null) {
				this.veiculo = veiculoFacade.recuperarPorIdentificacao(veiculo.getIdentificacao());
				if (veiculo == null) {
					JsfUtil.addMsgErro("Veículo não cadastrado.");
				} else {
					System.out.println("encontrou o veículo");
				}
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar veículo: " + e.getMessage());
		}
	}

	public Veiculo getVeiculo() {
		if (veiculo == null) {
			veiculo = new Veiculo();
		}
		return veiculo;
	}

	public List<AnaliseViagem> getAnalises() {
		return analises;
	}
	
	public AnaliseViagem getAnaliseNoMapa() {
		return analiseNoMapa;
	}

	public double[] getTotal() {
		return total;
	}

	public void exibirTabela() {
		this.exibicao = EXIBICAO_TABELA;
		this.analiseNoMapa = null;
	}

	public Boolean getExibicaoTabela() {
		return this.exibicao.equals(EXIBICAO_TABELA);
	}
	public Boolean getExibicaoMapa() {
		return this.exibicao.equals(EXIBICAO_MAPA);
	}

	public void exibirMapa(AnaliseViagem analise) {
		this.analiseNoMapa = analise;
		this.exibicao = EXIBICAO_MAPA;
		this.mapModel = new DefaultMapModel();
		resetarMapa();
		criarMarcadores();
		criarLinhas();
	}

	private void criarLinhas() {
		Polyline polyline = new Polyline();
		for (PontoRota ponto: analiseNoMapa.getProgramacao().getRota().getPontos()) {
			LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(7);  
		polyline.setStrokeColor("#0000FF");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);  
	}

	private void criarMarcadores() {
		for (AnalisePosicao analise: analiseNoMapa.getAnalisesPosicao()) {
			criarMarcador(analise);
		}
	}

	private void resetarMapa() {
		if (analiseNoMapa == null) {
			this.centroMapa = "-24.753573,-51.762526";
		} else {
			PontoRota primeiro = analiseNoMapa.getProgramacao().getRota().getPontos().get(0);
			this.centroMapa = 
					primeiro.getLat() + ", " + primeiro.getLng();
		} 
		this.zoomMapa = 13;
	}

	private void criarMarcador(AnalisePosicao analise) {
		String icone;
		String legenda;
		if (analise.getNoTrajeto()) {
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

	public String getExibicao() {
		return exibicao;
	}

	public void analisar() {
		try {
			this.analises = analisadorFacade.criarAnalises(dataInicial);
			totalizar();
			exibirTabela();
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}

	private void totalizar() {
		total = new double[6];
		for (AnaliseViagem analise: analises) {
			total[0] += analise.getKmPrevisto();
			total[1] += analise.getKmNoTrajeto();
			total[2] += analise.getKmNoTrajeto() - analise.getKmPrevisto();
			total[3] += analise.getKmForaTrajeto();
			total[4] += analise.getParadasPrevistas();
			total[5] += analise.getParadasCumpridas();
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