package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Atendido;
import modelo.Passageiro;
import modelo.PontoRota;
import modelo.ProgramacaoRota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.MapModel;

import util.JsfUtil;
import util.Paginador;
import util.RotaMapModel;
import facade.AtendidoFacade;
import facade.PontoRotaFacade;
import facade.RotaFacade;

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
	private Passageiro passageiroPesquisa;
	@EJB
	private AtendidoFacade facade;
	@EJB
	private RotaFacade rotaFacade;
	@EJB
	private PontoRotaFacade pontoRotaFacade;
	private Paginador paginador;
	private RotaMapModel rotaMapModel;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
		this.rotaMapModel = new RotaMapModel();
		rotaMapModel.exibirPontoPassagem(false);
		rotaMapModel.setDraggable(false);
	}

	public Atendido getAtendido() {
		return atendido;
	}

	public void setAtendido(Atendido atendido) {
		this.atendido = atendido;
	}

	public void listar() { 
		try {
			if (programacaoRotaPesquisa == null && passageiroPesquisa == null) {
				this.lista = facade.listar(paginador);
			} else if (programacaoRotaPesquisa != null && passageiroPesquisa == null) {
				this.lista = facade.listar(programacaoRotaPesquisa, paginador);
			} else if (programacaoRotaPesquisa == null && passageiroPesquisa != null) {
				this.lista = facade.listar(passageiroPesquisa, paginador);
			} else if (programacaoRotaPesquisa != null && passageiroPesquisa != null) {
				this.lista = facade.listar(programacaoRotaPesquisa, passageiroPesquisa, paginador);
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
		sincronizarMarcadores(true);
		sincronizarOpcoesDeParada();
	}

	public void iniciarAlteracao(Atendido atendido) {
		this.atendido = atendido;
		this.estadoView = ALTERACAO;
		sincronizarMarcadores(true);
		rotaMapModel.centralizarPeloPassageiro();
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
		sincronizarMarcadores(true);
		rotaMapModel.centralizarPeloPassageiro();
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

	public Passageiro getPassageiroPesquisa() {
		return passageiroPesquisa;
	}

	public void setPassageiroPesquisa(Passageiro passageiro) {
		this.passageiroPesquisa = passageiro;
	}

	public List<PontoRota> getParadas() {
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
		rotaMapModel.onMapStateChange(event);
	}

	public MapModel getMapModel() {
		return rotaMapModel.getMapModel();
	}

	public String getCentroMapa() {
		return rotaMapModel.getCentro();
	}

	public Integer getZoomMapa() {
		return rotaMapModel.getZoom();
	}

	public void programacaoRotaChange() {
		sincronizarOpcoesDeParada();
		sincronizarMarcadores(false);
		rotaMapModel.centralizarPelaRota();
	}

	public void passageiroChange() {
		sincronizarMarcadores(false);
		rotaMapModel.centralizarPeloPassageiro();
	}


	private void sincronizarMarcadores(boolean resetar) {
		if (resetar) {
			rotaMapModel.inicializarPropriedadesDeMapa();
		}
		rotaMapModel.setPassageiro(atendido.getPassageiro());
		if (atendido.getProgramacaoRota() != null) {
			try {
				rotaMapModel.setRota(rotaFacade.recuperarParaEdicao(atendido.getProgramacaoRota().getRota().getId()));
			} catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao exibir marcadores no mapa: " + e.getMessage());
			}
		} else {
			rotaMapModel.setRota(null);
		}
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
}