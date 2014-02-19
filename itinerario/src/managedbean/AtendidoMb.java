package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Atendido;
import modelo.Pessoa;
import modelo.PontoRota;
import modelo.ProgramacaoRota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.AtendidoFacade;
import facade.PontoRotaFacade;

@ManagedBean
@ViewScoped
public class AtendidoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Atendido atendido;
	private List<Atendido> lista;
	private List<PontoRota> paradas;
	private String estadoView;
	private ProgramacaoRota programacaoRotaPesquisa;
	private Pessoa pessoaPesquisa;
	@EJB
	private AtendidoFacade facade;
	@EJB
	private PontoRotaFacade pontoRotaFacade;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		resetarMapa();
	}

	public Atendido getAtendido() {
		return atendido;
	}

	public void setAtendido(Atendido atendido) {
		this.atendido = atendido;
	}

	public void listar() { 
		try {
			if (programacaoRotaPesquisa == null && pessoaPesquisa == null) {
				this.lista = facade.listar();
			} else if (programacaoRotaPesquisa != null && pessoaPesquisa == null) {
				this.lista = facade.listar(programacaoRotaPesquisa);
			} else if (programacaoRotaPesquisa == null && pessoaPesquisa != null) {
				this.lista = facade.listar(pessoaPesquisa);
			} else if (programacaoRotaPesquisa != null && pessoaPesquisa != null) {
				this.lista = facade.listar(programacaoRotaPesquisa, pessoaPesquisa);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Atendido> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.atendido = new Atendido();
		sincronizarMarcadores();
		sincronizarOpcoesDeParada();
	}

	public void iniciarAlteracao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = ALTERACAO;
		sincronizarMarcadores();
		sincronizarOpcoesDeParada();
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(atendido);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = EXCLUSAO;
		sincronizarMarcadores();
		sincronizarOpcoesDeParada();
	}

	public void terminarExclusao() {
		try {
			facade.excluir(atendido);
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

	public ProgramacaoRota getProgramacaoRotaPesquisa() {
		return programacaoRotaPesquisa;
	}

	public void setProgramacaoRotaPesquisa(ProgramacaoRota programacaoRota) {
		this.programacaoRotaPesquisa = programacaoRota;
	}

	public Pessoa getPessoaPesquisa() {
		return pessoaPesquisa;
	}

	public void setPessoaPesquisa(Pessoa pessoa) {
		this.pessoaPesquisa = pessoa;
	}

	public List<PontoRota> getParadas() {
		System.out.println("getParadas() " + paradas);
		return paradas;
	}

	private void sincronizarOpcoesDeParada() {
		try {
			if (atendido != null && atendido.getProgramacaoRota() != null) {
				paradas = pontoRotaFacade.recuperarParadas(atendido.getProgramacaoRota().getRota());
			} else {
				paradas = null;
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao carregar paradas da rota: " + e.getMessage());
		}
	}

	public void mapOnStateChange(StateChangeEvent event) {
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

	public void programacaoRotaChange() {
		sincronizarOpcoesDeParada();
		sincronizarMarcadores();
	}

	public void pessoaChange() {
		sincronizarMarcadores();
	}


	private void resetarMapa() {
		this.centroMapa = "-24.753573,-51.762526";
		this.zoomMapa = 15;
		this.mapModel = new DefaultMapModel();
	}

	private void sincronizarMarcadores() {
		try {
			mapModel = new DefaultMapModel();

			if (atendido != null && atendido.getPessoa() != null) {
				criarMarcadorPessoa(atendido.getPessoa());
				if (atendido.getPessoa().getLat() != null && atendido.getPessoa().getLng() != null) {
					this.centroMapa = atendido.getPessoa().getLat() + (", ") + (atendido.getPessoa().getLng());
				}
			}

			if (atendido != null && atendido.getProgramacaoRota() != null) {
				//int cont = 1;
				Polyline polyline = new Polyline();
				List<PontoRota> pontos = pontoRotaFacade.recuperarPontos(atendido.getProgramacaoRota().getRota());
				for (PontoRota ponto: pontos) {
					LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
					polyline.getPaths().add(latLng);
					/*
					if (cont == 1) {
						criarMarcadorDeInicio(ponto);
					} else if (cont == pontos.size()) {
						criarMarcadorDeTermino(ponto);
					} else if (ponto.getParada()) {
						criarMarcadorDeParada(ponto);
					}
					cont++;
					 */
					if (ponto.getParada()) {
						criarMarcadorDeParada(ponto);
					}				
				}
				polyline.setStrokeWeight(7);  
				polyline.setStrokeColor("#0000FF");  
				polyline.setStrokeOpacity(0.3);  
				mapModel.addOverlay(polyline);  
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao exibir marcadores no mapa: " + e.getMessage());
		}
	}


	private void criarMarcadorDeParada(PontoRota pontoRota) {
		String numero = null;
		if (pontoRota.getNumeroParada() < 10) {
			numero = "0" + pontoRota.getNumeroParada();
		} else {
			numero = String.valueOf(pontoRota.getNumeroParada());
		}
		String icone = "black" + numero+".png";
		LatLng latLng = new LatLng(pontoRota.getLat(), 
				pontoRota.getLng());
		Marker marker = new Marker(latLng, "", pontoRota);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Parada " + pontoRota.getNumeroParada();
		if (pontoRota.getDescricao() != null) {
			titulo = titulo.concat("\n")
					.concat(pontoRota.getDescricao());
		}
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcadorPessoa(Pessoa pessoa) {
		String icone;
		icone = "male-2.png";

		LatLng latLng = new LatLng(pessoa.getLat(), pessoa.getLng());
		Marker marker = new Marker(latLng, "", pessoa);
		marker.setIcon("resources/icones/" + icone);
		String titulo = pessoa.getNome();
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}
}