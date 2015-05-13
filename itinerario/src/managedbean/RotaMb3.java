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

import modelo.ArquivoImagem;
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

import util.ArquivoUtil;
import util.JsfUtil;
import util.Paginador;
import util.RotaMapModel;
import facade.ArquivoImagemFacade;
import facade.RotaFacade;

@ManagedBean
@ViewScoped
public class RotaMb3 implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Rota rota;
	private List<Rota> lista;
	private String estadoView;
	private String chavePesquisa;
	@EJB
	private RotaFacade facade;
	@EJB
	private ArquivoImagemFacade arquivoImagemFacade;
	private Paginador paginador;
	private PontoRota pontoReferencia;
	private RotaMapModel rotaMapModel;
	private ArquivoImagem arquivoImagem;
	private String caminhoImagem;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
		this.rotaMapModel = new RotaMapModel();
		this.arquivoImagem = new ArquivoImagem();
		sincronizarMapModel2(true);
		System.out.println("RotaMb.inicializar()");
	}

	public Rota getrota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
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
		this.rota.setImagens(new ArrayList<ArquivoImagem>());
		this.rota.setAtiva(true);
		this.caminhoImagem = null;
		this.arquivoImagem = new ArquivoImagem();
		this.pontoReferencia = null;
		sincronizarMapModel2(true);
	}

	public void iniciarAlteracao(Rota rota) {
		try {
			this.rota = facade.recuperarParaEdicao(rota.getId());
			this.pontoReferencia = null;
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemRota(rota);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			sincronizarMapModel2(true);
			rotaMapModel.centralizarPelaRota();
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar rota para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			if (arquivoImagem.getConteudo() == null) {
				rota.getImagens().clear();
			} else {
				if (rota.getImagens().size() == 0) {
					arquivoImagem.setRota(rota);
					rota.getImagens().add(0, arquivoImagem);
				} else {
					rota.getImagens().get(0).setConteudo(arquivoImagem.getConteudo());
				}
			}
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
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemRota(rota);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			sincronizarMapModel2(true);
			rotaMapModel.centralizarPelaRota();
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

	public void arquivoRotaCarregado(FileUploadEvent event) {
		UploadedFile arquivo = event.getFile();
		System.out.println("Nome do arquivo: " + arquivo.getFileName());
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
		return rotaMapModel.getMapModel();
	}

	public String getCentroMapa() {
		return rotaMapModel.getCentro();
	}

	public Integer getZoomMapa() {
		return rotaMapModel.getZoom();
	}

	private void sincronizarMapModel2(boolean resetar) {
		rotaMapModel.setRota(rota);
		if (resetar) {
			rotaMapModel.inicializarPropriedadesDeMapa();
		}
	}

	public List<Rota> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para rota: " + e.getMessage());
		}
		return null;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void limparImagem() {
		this.arquivoImagem.setConteudo(null);
		this.caminhoImagem = "";
	}

	public void arquivoImagemCarregado(FileUploadEvent event) {
		arquivoImagem.setConteudo(event.getFile().getContents());
		try {
			this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
		}
	}
}