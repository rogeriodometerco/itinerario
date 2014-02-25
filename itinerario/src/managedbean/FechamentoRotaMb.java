package managedbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.FechamentoRota;
import modelo.PontoRota;
import modelo.Rota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.AnoMes;
import util.JsfUtil;
import facade.AnalisePosicaoFacade;
import facade.AnaliseViagemFacade;
import facade.FechamentoRotaFacade;
import facade.PontoRotaFacade;

@ManagedBean
@ViewScoped
public class FechamentoRotaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private static final String MAPA = "mapa";
	private FechamentoRota fechamentoRota;
	private AnaliseViagem analiseNoMapa;
	private AnoMes anomes;
	private List<FechamentoRota> lista;
	private List<AnaliseViagem> analisesViagem;
	private String estadoView;
	private Rota rotaPesquisa;
	private Date dataInicialPesquisa;
	private Date dataFinalPesquisa;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	private double[] total;
	@EJB
	private FechamentoRotaFacade facade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;
	@EJB
	private PontoRotaFacade pontoRotaFacade;
	@EJB
	private AnalisePosicaoFacade analisePosicaoFacade;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.anomes = new AnoMes();
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 1);
		dataInicialPesquisa = new Date(c.getTimeInMillis());
		c.set(2013, 10, 30);
		dataFinalPesquisa = new Date(c.getTimeInMillis());
		resetarMapa();
	}

	public FechamentoRota getFechamentoRota() {
		return fechamentoRota;
	}

	public void setFechamentoRota(FechamentoRota fechamentoRota) {
		this.fechamentoRota = fechamentoRota;
	}

	public void listar() {
		if (dataInicialPesquisa == null) {
			JsfUtil.addMsgErro("Informe a data inicial para pesquisar");
			return;
		}
		if (dataFinalPesquisa == null) {
			JsfUtil.addMsgErro("Informe a data final para pesquisar");
			return;
		}

		try {
			if (rotaPesquisa == null) {
				this.lista = facade.getFechamentosRota(dataInicialPesquisa, dataFinalPesquisa);
			} else {
				this.lista = new ArrayList<FechamentoRota>();
				lista.add(facade.getFechamentoRota(rotaPesquisa, dataInicialPesquisa, dataFinalPesquisa));
			}
			//multiplicarDados(); // simula quantidade de dados real
			totalizar();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar relação de fechamentos de rota: " + e.getMessage());
		}
	}

	/*
	 * Aumenta quantidade de dados a exibir, para simular uma situação real.
	 */
	private void multiplicarDados() {
		while (lista.size() < 100) {
			lista.add(lista.get(0));
		}
		for (FechamentoRota f: lista) {
			if (f.getAnalisesViagem().size() > 0) {
				while (f.getAnalisesViagem().size() < 30 ) {
					f.getAnalisesViagem().addAll(f.getAnalisesViagem());
				}
			}
		}
		System.out.println("multiplicarDados()");
	}

	private void totalizar() {
		total = new double[8];
		for (FechamentoRota f: lista) {
			total[0] += f.getKmPrevisto();
			total[1] += f.getKmRealizado();
			total[2] += f.getKmNoTrajeto();
			total[3] += f.getKmForaTrajeto();
			total[4] += f.getKmPago();
			total[5] += f.getValorPago();
			total[6] += f.getParadasPrevistas();
			total[7] += f.getParadasCumpridas();
		}
	}

	public List<FechamentoRota> getLista() {
		return lista;
	}
	
	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.fechamentoRota = new FechamentoRota();
	}

	public void iniciarAlteracao(FechamentoRota fechamentoRota) {
		this.fechamentoRota = fechamentoRota;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(fechamentoRota);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(FechamentoRota fechamentoRota) {
		this.fechamentoRota = fechamentoRota;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(fechamentoRota);
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

	public Boolean isMapa() {
		return this.estadoView != null && this.estadoView.equals(MAPA);
	}

	public Rota getRotaPesquisa() {
		return rotaPesquisa;
	}

	public void setRotaPesquisa(Rota rota) {
		this.rotaPesquisa = rota;
	}

	public Date getDataInicialPesquisa() {
		return dataInicialPesquisa;
	}

	public void setDataInicialPesquisa(Date dataInicialPesquisa) {
		this.dataInicialPesquisa = dataInicialPesquisa;
	}

	public Date getDataFinalPesquisa() {
		return dataFinalPesquisa;
	}

	public void setDataFinalPesquisa(Date dataFinalPesquisa) {
		this.dataFinalPesquisa = dataFinalPesquisa;
	}

	public double[] getTotal() {
		return total;
	}

	public List<AnaliseViagem> getAnalisesViagem() {
		return analisesViagem;
	}

	public void setAnalisesViagem(List<AnaliseViagem> analisesViagem) {
		this.analisesViagem = analisesViagem;
	}  

	/*	public void onRowToggle(ToggleEvent event) {
    	analisesViagem = ((FechamentoRota)event.getData()).getAnalisesViagem();
    }
	 */
	
	/*
	public void salvar(List<AnaliseViagem> lista) {
		try {
			System.out.println("salvar(lista) "  +lista.get(0).getKmPago());
			System.out.println("salvar(lista) "  +this.lista.get(0).getAnalisesViagem().get(0).getKmPago());
			analiseViagemFacade.salvar(lista);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso");
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar análises: " + e.getMessage());
		}
	} 
	*/
	
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

	public void exibirMapa(AnaliseViagem analise) {
		this.analiseNoMapa = analise;
		this.estadoView = MAPA;
		this.mapModel = new DefaultMapModel();
		resetarMapa();
		criarMarcadores();
		criarLinhas();
	}

	public void exibirTabela() {
		this.estadoView = ALTERACAO;
	}
	
	private void criarLinhas() {
		try {
		Polyline polyline = new Polyline();
		for (PontoRota ponto: pontoRotaFacade.recuperarPontos(analiseNoMapa.getProgramacao().getRota())) {
			LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(7);  
		polyline.setStrokeColor("#0000FF");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao criar linhas: " + e.getMessage());
		}
	}

	private void criarMarcadores() {
		try {
		for (AnalisePosicao analise: analisePosicaoFacade.recuperarAnalisesPosicao(analiseNoMapa)) {
			criarMarcador(analise);
		}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao criar marcadores: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void resetarMapa() {
		this.centroMapa = "-24.753573,-51.762526";
		PontoRota primeiro = null;
		if (analiseNoMapa != null) {
			try {
				primeiro = pontoRotaFacade.recuperarPontos(analiseNoMapa.getProgramacao().getRota()).get(0);
				this.centroMapa = 
						primeiro.getLat() + ", " + primeiro.getLng();
			} catch (Exception e) {
				JsfUtil.addMsgErro("Um erro ocorreu ao posicionar o mapa: " + e.getMessage());
			}
		}
		this.zoomMapa = 15;
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
				+ analise.getPosicaoVeiculo().getVeiculo().getPlaca()
				+ "\nData e hora: " 
				+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(analise.getPosicaoVeiculo().getDataHora())
		+ "\nVelocidade: " 
		+ analise.getPosicaoVeiculo().getVelocidade() + " km/h";
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}


	public AnoMes getAnomes() {
		return anomes;
	}


	public void setAnomes(AnoMes anomes) {
		this.anomes = anomes;
	}

}