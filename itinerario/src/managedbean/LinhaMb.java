package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Linha;
import modelo.PontoLinha;
import motor.MensagemRMC;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.LinhaFacade;

@ManagedBean
@ViewScoped
public class LinhaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Linha linha;
	private List<Linha> lista;
	private String estadoView;
	@EJB
	private LinhaFacade facade;

	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		sincronizarMapModel();
	}

	public void onMapStateChange(StateChangeEvent event) {
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

	private void sincronizarMapModel() {
		mapModel = new DefaultMapModel();
		if (linha != null && linha.getPontos().size() > 0) {
			Polyline polyline = new Polyline();
			for (PontoLinha ponto: linha.getPontos()) {
				LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
				polyline.getPaths().add(latLng);
			}
			polyline.setStrokeWeight(7);  
			polyline.setStrokeColor("#0000FF");  
			polyline.setStrokeOpacity(0.3);  
			mapModel.addOverlay(polyline);  
			this.centroMapa = 
					linha.getPontos().get(0).getLat() 
					+ ", " + linha.getPontos().get(0).getLng();
		} else {
			this.centroMapa = "-24.750573, -51.781526";
		}
		this.zoomMapa = 10;
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public void listar() { 
		try {
			this.lista = facade.listar();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Linha> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.linha = new Linha();
		this.linha.setPontos(new ArrayList<PontoLinha>());
		this.linha.setAtiva(true);
	}

	public void iniciarAlteracao(Linha linha) {
		try {
			this.linha = facade.recuperarParaEdicao(linha.getId());
			sincronizarMapModel();
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar linha para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(linha);
			JsfUtil.addMsgSucesso("Linha salva com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Linha linha) {
		this.linha = linha;
		sincronizarMapModel();
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(linha);
			JsfUtil.addMsgSucesso("Linha excluída com sucesso.");
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

	public void arquivoImportado(FileUploadEvent event) {
		UploadedFile arquivo = event.getFile(); 
		mapModel = new DefaultMapModel();
		int cont = 1;
		try {
			Scanner s = new Scanner(arquivo.getInputstream());
			PontoLinha pontoLinha = null;
			MensagemRMC mensagem = null;
			// TODO Excluir os pontos atuais para receber os novos pontos.
			// O código abaixo não está removendo os pontos após salvar o objeto.
			while (linha.getPontos().size() > 0) {
				linha.getPontos().remove(0);
			}
			while (s.hasNext()) {
				mensagem = new MensagemRMC(s.next());
				pontoLinha = new PontoLinha();
				pontoLinha.setLat(mensagem.getLat());
				pontoLinha.setLng(mensagem.getLng());
				pontoLinha.setSequencia(cont);
				pontoLinha.setLinha(this.linha);
				this.linha.getPontos().add(pontoLinha);
				//System.out.println(mensagem.toString());
				cont ++;
			}
			sincronizarMapModel();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao importar arquivo. Linha " + cont + ". " + e.getMessage());
			e.printStackTrace();
		}
		JsfUtil.addMsgSucesso(arquivo.getFileName() 
				+ " carregado. " + linha.getPontos().size() + " pontos importados.");
	}  
}