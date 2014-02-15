package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import modelo.Atendido;
import modelo.Pessoa;
import modelo.PontoRota;
import modelo.ProgramacaoRota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

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
	private List<SelectItem> paradas;
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
	}

	public void iniciarAlteracao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = ALTERACAO;
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

	public List<SelectItem> getParadas() {
		return paradas;
	}

	private void carregarParadas() {
		try {
			paradas = new ArrayList<SelectItem>();
			if (atendido.getProgramacaoRota() != null) {
				int cont = 1;
				for (PontoRota p: pontoRotaFacade.recuperarParadas(atendido.getProgramacaoRota().getRota())) {
					paradas.add(new SelectItem(p, String.valueOf(cont++).concat(" - " ).concat(p.getDescricao())));
				}
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
		carregarParadas();
		sincronizarMarcadores();
	}

	public void pessoaChange() {
		carregarParadas();
		sincronizarMarcadores();
	}


	private void resetarMapa() {
		this.centroMapa = "-24.753573,-51.762526";
		this.zoomMapa = 13;
		this.mapModel = new DefaultMapModel();
	}

	private void sincronizarMarcadores() {
		mapModel = new DefaultMapModel();
		int parada = 1;
		for (SelectItem i: paradas) {
			criarMarcadorParada((PontoRota)i.getValue(), parada++);
		}

		if (atendido != null && atendido.getPessoa() != null) {
			criarMarcadorPessoa(atendido.getPessoa());
			if (atendido.getPessoa().getLat() != null && atendido.getPessoa().getLng() != null) {
				this.centroMapa = atendido.getPessoa().getLat() + (", ") + (atendido.getPessoa().getLng());
			}
		}

	}


	private void criarMarcadorParada(PontoRota ponto, int numeroParada) {
		String icone;
		icone = "mm_20_red.png";

		LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
		Marker marker = new Marker(latLng, "", ponto);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Parada: " + numeroParada
				+ "\n" + ponto.getDescricao()
				+ "\n" + marker.getLatlng().toString();
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}

	private void criarMarcadorPessoa(Pessoa pessoa) {
		String icone;
		icone = "mm_20_white.png";

		LatLng latLng = new LatLng(pessoa.getLat(), pessoa.getLng());
		Marker marker = new Marker(latLng, "", pessoa);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "pessoa.getNome"
				+ "\n" + marker.getLatlng().toString();
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}
}