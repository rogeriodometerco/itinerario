package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.AgendamentoLinha;
import modelo.Linha;
import modelo.PontoLinha;
import modelo.ProgramacaoLinha;
import modelo.Veiculo;
import motor.MensagemRMC;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.LinhaFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class LinhaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Linha linha;
	private AgendamentoLinha agendamento;
	private List<Linha> lista;
	private String estadoView;

	@EJB
	private LinhaFacade facade;

	@EJB
	private VeiculoFacade veiculoFacade;

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
/*
	public List<ProgramacaoLinha> getProgramacoes() {
		return linha.getProgramacoes();
	}
	
	public void setProgramacoes(List<ProgramacaoLinha> lista) {
		this.linha.setProgramacoes(lista);
	}*/
	
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
				if (ponto.getParada()) {
					criarMarcadorDeParada(ponto);
				}
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

	private void criarMarcadorDeParada(PontoLinha pontoLinha) {
		String icone = "mm_20_white.png";
		LatLng latLng = new LatLng(pontoLinha.getLat(), 
				pontoLinha.getLng());
		Marker marker = new Marker(latLng, "", pontoLinha);
		marker.setIcon("resources/icones/" + icone);
		String titulo = "Ponto de parada";
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		System.out.println("setLinha() ");
		this.linha = linha;
	}

	public AgendamentoLinha getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(AgendamentoLinha agendamento) {
		this.agendamento = agendamento;
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
		this.linha.setAgendamentos(new ArrayList<AgendamentoLinha>());
		this.linha.setProgramacoes(new ArrayList<ProgramacaoLinha>());
		this.linha.setAtiva(true);
	}

	public void iniciarAlteracao(Linha linha) {
		try {
			this.linha = facade.recuperarParaEdicaoOuExclusao(linha.getId());
			sincronizarMapModel();
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar linha para edi��o: " + e.getMessage());
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

	public void inicializarVeiculo(ProgramacaoLinha programacao) {
		try {
			String identificacao = programacao.getVeiculo().getIdentificacao();
			programacao.setVeiculo(veiculoFacade.recuperarPorIdentificacao(programacao.getVeiculo().getIdentificacao()));
			if (programacao.getVeiculo() == null) {
				// Volta o estado do ve�culo.
				programacao.setVeiculo(new Veiculo());
				programacao.getVeiculo().setIdentificacao(identificacao);
				
				JsfUtil.addMsgErro("Veiculo " + programacao.getVeiculo().getIdentificacao() 
						+ " n�o cadastrado.");
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao consultar ve�culo: " + e.getMessage());
		}
	}

	public void iniciarExclusao(Linha linha) {
		try {
			this.linha = facade.recuperarParaEdicaoOuExclusao(linha.getId());
			sincronizarMapModel();
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar linha para exclus�o: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(linha);
			JsfUtil.addMsgSucesso("Linha exclu�da com sucesso.");
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

	public void novoAgendamento() {
		System.out.println("novoAgendamento()");
		AgendamentoLinha agendamento = new AgendamentoLinha();
		agendamento.setLinha(linha);
		linha.getAgendamentos().add(agendamento);
	}

	public void removerAgendamento(AgendamentoLinha agendamento) {
		this.linha.getAgendamentos().remove(agendamento);
	}

	public void novaProgramacao() {
		System.out.println("novaProgramacao()");
		ProgramacaoLinha programacao = new ProgramacaoLinha();
		// Inicializa alguns atributos.
		if (!linha.getProgramacoes().isEmpty()) {
				copiarAtributos(linha.getProgramacoes().get(
							linha.getProgramacoes().size()-1), programacao);
		} else {
			programacao.setDiaSemana(Calendar.MONDAY);
			programacao.setVeiculo(new Veiculo());
			programacao.setLinha(linha);
		}
		linha.getProgramacoes().add(programacao);
	}

	private void copiarAtributos(ProgramacaoLinha de, ProgramacaoLinha para) {
		para.setDiaSemana(de.getDiaSemana() + 1);
		para.setHoraInicial(de.getHoraInicial());
		para.setHoraFinal(de.getHoraFinal());
		para.setLinha(de.getLinha());
		para.setVeiculo(de.getVeiculo());
	}
	
	public void removerProgramacao(ProgramacaoLinha programacao) {
		this.linha.getProgramacoes().remove(programacao);
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
			// O c�digo abaixo n�o est� removendo os pontos ap�s salvar o objeto.
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
				pontoLinha.setParada(mensagem.getVelocidade() == 0);
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