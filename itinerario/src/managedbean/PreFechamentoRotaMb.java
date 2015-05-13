package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.AnaliseViagem;
import modelo.Rota;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.MapModel;

import util.AnoMes;
import util.JsfUtil;
import util.RotaMapModel;
import dto.PreFechamentoRota;
import facade.AnalisePosicaoFacade;
import facade.AnaliseViagemFacade;
import facade.FechamentoRotaFacade;
import facade.PontoRotaFacade;
import facade.RotaFacade;

@ManagedBean
@ViewScoped
public class PreFechamentoRotaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private static final String MAPA = "mapa";
	private PreFechamentoRota preFechamentoRota;
	private AnaliseViagem analiseNoMapa;
	private AnoMes anomes;
	private List<PreFechamentoRota> lista;
	private List<AnaliseViagem> analisesViagem;
	private String estadoView;
	private Rota rotaPesquisa;
	private Date dataInicialPesquisa;
	private Date dataFinalPesquisa;
	private RotaMapModel rotaMapModel;
	private double[] total;
	@EJB
	private FechamentoRotaFacade facade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;
	@EJB
	private PontoRotaFacade pontoRotaFacade;
	@EJB
	private AnalisePosicaoFacade analisePosicaoFacade;
	@EJB
	private RotaFacade rotaFacade;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.anomes = new AnoMes();
		this.rotaMapModel = new RotaMapModel();
		rotaMapModel.setDraggable(false);
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 1);
		dataInicialPesquisa = new Date(c.getTimeInMillis());
		c.set(2013, 10, 30);
		dataFinalPesquisa = new Date(c.getTimeInMillis());
	}

	public PreFechamentoRota getPreFechamentoRota() {
		return preFechamentoRota;
	}

	public void setPreFechamentoRota(PreFechamentoRota preFechamentoRota) {
		this.preFechamentoRota = preFechamentoRota;
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
				this.lista = facade.getPreFechamentosRota(dataInicialPesquisa, dataFinalPesquisa);
			} else {
				this.lista = facade.getPreFechamentosRota(rotaPesquisa, dataInicialPesquisa, dataFinalPesquisa);
			}
			//multiplicarDados(); // simula quantidade de dados real
			totalizar();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar relação de fechamentos de rota: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * Aumenta quantidade de dados a exibir, para simular uma situação real.
	 */
	private void multiplicarDados() {
		while (lista.size() < 100) {
			lista.add(lista.get(0));
		}
		for (PreFechamentoRota f: lista) {
			if (f.getAnalisesViagem().size() > 0) {
				while (f.getAnalisesViagem().size() < 30 ) {
					f.getAnalisesViagem().addAll(f.getAnalisesViagem());
				}
			}
		}
		System.out.println("multiplicarDados()");
	}

	private void totalizar() {
		total = new double[10];
		for (PreFechamentoRota f: lista) {
			total[0] += f.getQtdeViagensPrevistas();
			total[1] += f.getQtdeViagensRealizadas();
			total[2] += f.getKmPrevisto();
			total[3] += f.getKmRealizado();
			total[4] += f.getKmNoTrajeto();
			total[5] += f.getKmForaTrajeto();
			total[6] += f.getKmPago();
			total[7] += f.getValorPago();
			total[8] += f.getParadasPrevistas();
			total[9] += f.getParadasCumpridas();
		}
	}

	public List<PreFechamentoRota> getLista() {
		return lista;
	}
	
	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.preFechamentoRota = new PreFechamentoRota();
	}

	public void iniciarAlteracao(PreFechamentoRota preFechamentoRota) {
		this.preFechamentoRota = preFechamentoRota;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			if (preFechamentoRota.getConcluido()) {
				facade.criarFechamentoRota(preFechamentoRota);
			} else {
				analiseViagemFacade.salvar(preFechamentoRota.getAnalisesViagem());
			}
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
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

	public void onStateChange(StateChangeEvent event) {
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

	public void exibirMapa(AnaliseViagem analise) {
		try {
			this.analiseNoMapa = analise;
			rotaMapModel.setAnaliseViagem(analiseViagemFacade.recuperarParaEdicao(analiseNoMapa.getId()));
			rotaMapModel.setRota(rotaFacade.recuperarParaEdicao(analise.getRota().getId()));
			this.estadoView = MAPA;
			rotaMapModel.inicializarPropriedadesDeMapa();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao exibir mapa: " + e.getMessage());
		}
	}

	public void exibirTabela() {
		this.estadoView = ALTERACAO;
	}
	
	public AnoMes getAnomes() {
		return anomes;
	}


	public void setAnomes(AnoMes anomes) {
		this.anomes = anomes;
	}

}