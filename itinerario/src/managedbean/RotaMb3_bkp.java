package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.PontoRota;
import modelo.Rota;
import motor.MensagemRMC;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import util.Paginador;
import util.RotaMapModel;
import facade.RotaFacade;

@ManagedBean
@ViewScoped
public class RotaMb3_bkp implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Rota rota;
	private List<PontoRota> paradas;
	private List<Rota> lista;
	private String estadoView;
	private String chavePesquisa;
	@EJB
	private RotaFacade facade;
	private Paginador paginador;
	private MapModel mapModel;
	private PontoRota pontoReferencia;
	private RotaMapModel rotaMapModel;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
		this.rotaMapModel = new RotaMapModel();
		sincronizarMapModel2(true);
		System.out.println("RotaMb.inicializar()");
	}

	public Rota getrota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public List<PontoRota> getParadas() {
		if (paradas == null) {
			this.paradas = rota.getParadas();
		}
		// TODO desfazer
		this.paradas = rota.getPontos();
		return paradas;
	}

	public void setParadas(List<PontoRota> paradas) {
		for (PontoRota p: paradas) {
			for (PontoRota p2: rota.getPontos()) {
				if (p.getNumeroParada().equals(p2.getNumeroParada())) {
					rota.getPontos().set(rota.getPontos().indexOf(p2), p);
				}
			}
		}
		this.paradas = paradas;

	}

	public void listar() { 
		try {
			if (chavePesquisa == null || chavePesquisa.trim().length() == 0) {
				this.lista = facade.listar(paginador);
			} else {
				this.lista = facade.autocomplete(chavePesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Rota> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.rota = new Rota();
		this.rota.setPontos(new ArrayList<PontoRota>());
		this.rota.setAtiva(true);
		this.paradas = null;
		this.pontoReferencia = null;
		sincronizarMapModel2(true);
	}

	public void iniciarAlteracao(Rota rota) {
		try {
			this.rota = facade.recuperarParaEdicao(rota.getId());
			//this.paradas = rota.getParadas();
			this.pontoReferencia = null;
			sincronizarMapModel2(true);
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar rota para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(rota);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Rota rota) {
		try {
			this.rota = facade.recuperarParaExclusao(rota.getId());
			sincronizarMapModel2(true);
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar rota para exclusão: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(rota);
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

	public void arquivoCarregado(FileUploadEvent event) {
		UploadedFile arquivo = event.getFile();
		System.out.println("Nome do arquivo: " + arquivo.getFileName());
		mapModel = new DefaultMapModel();
		int linha = 1;
		try {
			Scanner s = new Scanner(arquivo.getInputstream());
			PontoRota pontoRota = null;
			MensagemRMC mensagem = null;
			// Remove os pontos atuais.
			rota.setPontos(new ArrayList<PontoRota>());
			Double latAnterior = 0d, lngAnterior = 0d;
			int numeroParada = 1;
			while (s.hasNext()) {
				mensagem = new MensagemRMC(s.next());
				// Teste para evitar inserir pontos repetidos.
				if (!latAnterior.equals(mensagem.getLat()) || !lngAnterior.equals(mensagem.getLng())) {
					pontoRota = new PontoRota();
					pontoRota.setLat(mensagem.getLat());
					pontoRota.setLng(mensagem.getLng());
					pontoRota.setSequencia(rota.getPontos().size()+1);
					pontoRota.setRota(rota);
					pontoRota.setParada(mensagem.getVelocidade() == 0);
					if (pontoRota.getParada()) {
						pontoRota.setNumeroParada(numeroParada++);
					}
					rota.getPontos().add(pontoRota);
					System.out.println(mensagem.toString());
				}
				latAnterior = mensagem.getLat();
				lngAnterior = mensagem.getLng();
				//System.out.println(mensagem.toString());
				linha++;
			}
			sincronizarMapModel2(true);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao importar arquivo. Linha " + linha + ". " + e.getMessage());
			e.printStackTrace();
		}
		JsfUtil.addMsgSucesso(arquivo.getFileName() 
				+ " carregado. " + linha + " linhas processadas, " + rota.getPontos().size() + " pontos criados.");
	}

	public void onMapStateChange(StateChangeEvent event) {
		rotaMapModel.onMapStateChange(event);
		//zoomMapa = event.getZoomLevel();
		//centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

	public void onPointSelect(PointSelectEvent event) {
		if (pontoReferencia != null || rota.getPontos().size() == 0) {
			PontoRota pontoRota = new PontoRota();
			pontoRota.setLat(event.getLatLng().getLat());
			pontoRota.setLng(event.getLatLng().getLng());
			pontoRota.setRota(rota);
			pontoRota.setParada(false);
			int sequencia = 1;
			if (pontoReferencia != null) {
				sequencia = pontoReferencia.getSequencia() + 1;
			}
			pontoRota.setSequencia(sequencia);
			if (rota.getPontos().size() == 0) {
				rota.getPontos().add(pontoRota);
			} else {
				rota.getPontos().add(pontoRota.getSequencia() - 1, pontoRota);
			}
			pontoReferencia = pontoRota;

			numerarPontos();
			sincronizarMapModel2(false);
		}
	}

	private void numerarPontos() {
		int sequencia;
		sequencia = 1;
		int numeroParada = 1;
		for (PontoRota p: rota.getPontos()) {
			p.setSequencia(sequencia++);
			if (p.getParada()) {
				p.setNumeroParada(numeroParada++);
			} else {
				p.setNumeroParada(null);
			}
		}
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		if (event.getOverlay() instanceof Marker) {
			pontoReferencia = (PontoRota)event.getOverlay().getData();
		}
	}

	public void onMarkerDrag(MarkerDragEvent event) {
		Marker marker = event.getMarker();
		PontoRota pontoRota = (PontoRota)marker.getData();
		pontoRota.setLat(marker.getLatlng().getLat());
		pontoRota.setLng(marker.getLatlng().getLng());
		sincronizarMapModel2(false);
	}

	public void removerPonto(PontoRota pontoRota) {
		rota.getPontos().remove(pontoRota);
		numerarPontos();
		sincronizarMapModel2(false);
	}
	
	public void pontoModificado() {
		numerarPontos();
		sincronizarMapModel2(false);
	}
	
	public MapModel getMapModel() {
		
		//return mapModel;
		return rotaMapModel.getMapModel();
	}

	public String getCentroMapa() {
		//return centroMapa;
		return rotaMapModel.getCentro();
	}

	public Integer getZoomMapa() {
		//return zoomMapa;
		return rotaMapModel.getZoom();
	}

	private void sincronizarMapModel2() {

		int cont = 1;
		//System.out.println(mapModel.getMarkers().size() + " marcadores antes");
		//mapModel = new DefaultMapModel();
		mapModel.getMarkers().clear();
		mapModel.getPolylines().clear();
		if (rota != null && rota.getPontos().size() > 0) {
			System.out.println(rota.getPontos().size() + " pontos");

			Collections.sort(rota.getPontos(), new Comparator<PontoRota>() {
				public int compare(PontoRota o1, PontoRota o2) {
					if (o1.getSequencia() < o2.getSequencia()) {
						return -1;
					} else if (o1.getSequencia() > o2.getSequencia()) {
						return 1;
					} else {					
						return 0;
					}
				}
			});

			Polyline polyline = new Polyline();
			for (PontoRota ponto: rota.getPontos()) {
				LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
				polyline.getPaths().add(latLng);
				criarMarcadorIntermediario(ponto);
				/*
				if (cont == 1) {
					criarMarcadorDeInicio(ponto);
				} else if (cont == rota.getPontos().size()) {
					criarMarcadorDeTermino(ponto);
				} else if (ponto.getParada()) {
					criarMarcadorDeParada(ponto);
				} else {
					criarMarcadorIntermediario(ponto);
				}
				 */
				cont++;
			}
			polyline.setStrokeWeight(7);  
			polyline.setStrokeColor("#0000FF");  
			polyline.setStrokeOpacity(0.3);  
			mapModel.addOverlay(polyline);  
		} 
		System.out.println(mapModel.getMarkers().size() + " marcadores depois");
	}

	private void sincronizarMapModel2(boolean resetar) {

		rotaMapModel.setRota(rota);
		if (resetar) {
			rotaMapModel.inicializarPropriedadesDeMapa();
		}
		/*
		if (mapModel == null) {
			mapModel = new DefaultMapModel();
		} else {
			mapModel.getMarkers().clear();
			mapModel.getPolylines().clear();
		}
		if (resetar) {
			if (rota != null && rota.getPontos().size() > 0) {
				this.centroMapa = 
						rota.getPontos().get(0).getLat() 
						+ ", " + rota.getPontos().get(0).getLng();
			} else {
				this.centroMapa = "-24.754737, -51.764410";
			}
			this.zoomMapa = 14;
		}

		if (rota != null && rota.getPontos().size() > 0) {
			System.out.println(rota.getPontos().size() + " pontos");

			Collections.sort(rota.getPontos(), new Comparator<PontoRota>() {
				public int compare(PontoRota o1, PontoRota o2) {
					if (o1.getSequencia() < o2.getSequencia()) {
						return -1;
					} else if (o1.getSequencia() > o2.getSequencia()) {
						return 1;
					} else {					
						return 0;
					}
				}
			});

			Polyline polyline = new Polyline();
			for (PontoRota ponto: rota.getPontos()) {
				LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
				polyline.getPaths().add(latLng);
				criarMarcadorIntermediario(ponto);
			}
			polyline.setStrokeWeight(7);  
			polyline.setStrokeColor("#0000FF");  
			polyline.setStrokeOpacity(0.3);  
			mapModel.addOverlay(polyline);  
		}
		*/ 
	}

	private void sincronizarMapModel() {
		/*
		int cont = 1;
		mapModel = new DefaultMapModel();
		if (rota != null && rota.getPontos().size() > 0) {
			Polyline polyline = new Polyline();
			for (PontoRota ponto: rota.getPontos()) {
				LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
				polyline.getPaths().add(latLng);
				if (cont == 1) {
					criarMarcadorDeInicio(ponto);
				} else if (cont == rota.getPontos().size()) {
					criarMarcadorDeTermino(ponto);
				} else if (ponto.getParada()) {
					criarMarcadorDeParada(ponto);
				} else {
					criarMarcadorIntermediario(ponto);
				}
				cont++;
			}
			polyline.setStrokeWeight(7);  
			polyline.setStrokeColor("#0000FF");  
			polyline.setStrokeOpacity(0.3);  
			mapModel.addOverlay(polyline);  
			this.centroMapa = 
					rota.getPontos().get(0).getLat() 
					+ ", " + rota.getPontos().get(0).getLng();
		} else {
			this.centroMapa = "-24.754737, -51.764410";
		}
		this.zoomMapa = 14;
		*/
	}

	private void criarMarcadorDeParada(PontoRota pontoRota) {
		String numero = null;
		if (pontoRota.getNumeroParada() < 10) {
			numero = "0" + pontoRota.getNumeroParada();
		} else {
			numero = String.valueOf(pontoRota.getNumeroParada());
		}
		String icone = "black" + numero+".png";
		//System.out.println(icone);
		LatLng latLng = new LatLng(pontoRota.getLat(), 
				pontoRota.getLng());
		Marker marker = new Marker(latLng, "", pontoRota);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Ponto de parada";
		marker.setTitle(titulo);
		marker.setData(pontoRota);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcadorDeInicio(PontoRota pontoRota) {
		//String icone = "start-race-2.png";
		String icone = pontoRota.getNumeroParada() + ".png";
		//System.out.println(icone);
		LatLng latLng = new LatLng(pontoRota.getLat(), 
				pontoRota.getLng());
		Marker marker = new Marker(latLng, "", pontoRota);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Ponto de início";
		marker.setTitle(titulo);
		marker.setData(pontoRota);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcadorDeTermino(PontoRota pontoRota) {
		//String icone = "finish.png";
		String icone = pontoRota.getNumeroParada() + ".png";
		//System.out.println(icone);
		LatLng latLng = new LatLng(pontoRota.getLat(), 
				pontoRota.getLng());
		Marker marker = new Marker(latLng, "", pontoRota);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Ponto de término";
		marker.setTitle(titulo);
		marker.setData(pontoRota);
		this.mapModel.addOverlay(marker);
	}	

	private void criarMarcadorIntermediario(PontoRota pontoRota) {
		//http://thydzik.com/thydzikGoogleMap/markerlink.php?text=12&color=5680FC
		String cor = null;

		if (pontoRota.getSequencia() == 1) {
			cor = RotaMapModel.COR_MARCADOR_INICIO;
		} else if (pontoRota.getSequencia() == rota.getPontos().size()) {
			cor = RotaMapModel.COR_MARCADOR_TERMINO;
		} else if (pontoRota.getParada()) {
			cor = RotaMapModel.COR_MARCADOR_PARADA;
		} else {
			cor = RotaMapModel.COR_MARCADOR_PASSAGEM;
		} 
		/*
		if (pontoRota.getParada()) {
			cor = COR_MARCADOR_PARADA;
		} else {
			cor = COR_MARCADOR_PASSAGEM;
		} 
		*/
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
		
		//System.out.println("descricao: " + pontoRota.getDescricao());
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
		marker.setDraggable(true);
		this.mapModel.addOverlay(marker);
	}

	public List<Rota> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para rota: " + e.getMessage());
		}
		return null;
	}

}