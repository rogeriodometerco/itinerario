package managedbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.PontoRota;
import modelo.Rota;
import modelo.Veiculo;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import util.RotaMapModel;
import facade.AnalisadorFacade;
import facade.AnalisePosicaoFacade;
import facade.AnaliseViagemFacade;
import facade.PontoRotaFacade;
import facade.RotaFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class AnaliseViagemMb {
	private Date dataViagem;
	private Date dataInicial;
	private Date dataFinal;
	private Veiculo veiculo;
	private Rota rota;
	private String exibicao;
	private RotaMapModel rotaMapModel;
//	private MapModel mapModel;
//	private String centroMapa;
//	private Integer zoomMapa;
	private double[] total;

	private List<AnaliseViagem> analises;
	private AnaliseViagem analiseNoMapa;

	@EJB
	private VeiculoFacade veiculoFacade;
	@EJB
	private RotaFacade rotaFacade;
	@EJB
	private AnalisadorFacade analisadorFacade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;
	@EJB
	private PontoRotaFacade pontoRotaFacade;
	@EJB
	private AnalisePosicaoFacade analisePosicaoFacade;

	public static String EXIBICAO_TABELA = "TABELA";
	public static String EXIBICAO_MAPA = "MAPA";

	@PostConstruct
	private void inicializar() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 25);
		dataInicial = new Date(c.getTimeInMillis());
		dataFinal = new Date(c.getTimeInMillis());
		veiculo = new Veiculo();
		rota = new Rota();
		rotaMapModel = new RotaMapModel();
		consultar();
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
			if (getVeiculo().getPlaca().length() > 0) {
				this.veiculo = veiculoFacade.recuperarPorPlaca(veiculo.getPlaca());
				if (veiculo == null) {
					JsfUtil.addMsgErro("Veículo não cadastrado.");
				}
			} else {
				veiculo = null; 
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar veículo: " + e.getMessage());
		}
	}
	
	public void codigoRotaChange() {
		try {
			if (rota.getCodigo().length() > 0) {
				this.rota = rotaFacade.recuperarPorCodigo(rota.getCodigo());
				if (rota == null) {
					JsfUtil.addMsgErro("Rota não cadastrada.");
				}
			} else {
				rota = null;
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar rota: " + e.getMessage());
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

	public void setAnalises(List<AnaliseViagem> analises) {
		this.analises = analises;
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
		rotaMapModel.setAnaliseViagem(analiseNoMapa);
		rotaMapModel.inicializarPropriedadesDeMapa();
		rotaMapModel.centralizarPelaAnaliseViagem();
	}

	public String getExibicao() {
		return exibicao;
	}

	public void processarAnalises() {
		if (dataViagem == null) {
			JsfUtil.addMsgErro("Informe a data para processar a análise das viagens.");
			return;
		}
		try {
			analisadorFacade.analisarViagens(dataViagem);
			JsfUtil.addMsgSucesso("Processo de análise das viagens concluído com sucesso.");
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao processar análises de viagem: " + e.getMessage());
		}
	}
	
	public void consultar() {
		try {
			if (dataInicial == null || dataFinal == null) {
				throw new Exception("Data inicial e final são obrigatórios para esta consulta.");
			}
			if (getVeiculo().getId() == null && getRota().getId() == null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal);
			} else if (getVeiculo().getId() != null && getRota().getId() == null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, veiculo);
			} else if (getVeiculo().getId() == null && getRota().getId() != null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, rota);
			} else {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, rota, veiculo);
			}
			/*
			if (dataInicial == null || dataFinal == null) {
				throw new Exception("Data inicial e final são obrigatórios para esta consulta.");
			}
			if (getVeiculo().getId() == null && rota == null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal);
			} else if (getVeiculo().getId() != null && rota == null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, veiculo);
			} else if (getVeiculo().getId() == null && rota != null) {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, rota);
			} else {
				this.analises = analiseViagemFacade.recuperarAnalisesViagem(dataInicial,  dataFinal, rota, veiculo);
			}*/
			

			totalizar();
			exibirTabela();
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}

	private void totalizar() {
		total = new double[8];
		for (AnaliseViagem analise: analises) {
			total[0] += analise.getKmPrevisto();
			total[1] += analise.getKmRealizado();
			total[2] += analise.getKmNoTrajeto();
			total[3] += analise.getKmNoTrajeto() - analise.getKmPrevisto();
			total[4] += analise.getKmForaTrajeto();
			total[5] += analise.getParadasPrevistas();
			total[6] += analise.getParadasCumpridas();
			if (analise.getKmPago() != null) {
				total[7] += analise.getKmPago();
			}
		}
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

	public Rota getRota() {
		if (rota == null) {
			rota = new Rota();
		}
		return rota;
	}

	public Date getDataViagem() {
		return dataViagem;
	}

	public void setDataViagem(Date dataViagem) {
		this.dataViagem = dataViagem;
	}

	public void salvar() {
		try {
			this.analiseViagemFacade.salvar(this.analises);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}
	}
	
	public void pagarKmPrevisto() {
		for (AnaliseViagem analise: analises) {
			pagarKmPrevisto(analise);
		}
		totalizar();
	}
	
	public void pagarKmPrevisto(AnaliseViagem analise) {
		analise.setKmPago(analise.getKmPrevisto());
		totalizar();
	}

	public void pagarKmRealizado() {
		for (AnaliseViagem analise: analises) {
			pagarKmRealizado(analise);
		}
		totalizar();
	}
	
	public void pagarKmRealizado(AnaliseViagem analise) {
		analise.setKmPago(analise.getKmRealizado());
		totalizar();
	}
	
	public void pagarKmNoTrajeto() {
		for (AnaliseViagem analise: analises) {
			pagarKmNoTrajeto(analise);
		}
		totalizar();
	}
	
	public void pagarKmNoTrajeto(AnaliseViagem analise) {
		analise.setKmPago(analise.getKmNoTrajeto());
		totalizar();
	}

	public void definirValorPago(AnaliseViagem analise) {
		analise.setValorPago(analise.getValorKm() * analise.getKmPago());
		totalizar();
	}
	
	public void alterouValor(ValueChangeEvent event) {
		//TODO Como atualizar o modelo para refletir o total quando o usuário digita no campo "km pago". 
		System.out.println("Alterou de " + event.getOldValue() + " para " + event.getNewValue());
				/*
		System.out.println("alterouValor(): " + analises.indexOf(analise) + analise.getKmPago());
		AnaliseViagem item = analises.get(analises.indexOf(analise));
		System.out.println(item.getId() + "- " + item.getKmPago());
		item.setKmPago(analise.getKmPago());
		analises.get(
				analises.indexOf(analise))
				.setKmPago(analise.getKmPago());
//		for (AnaliseViagem a: analises) {
//			System.out.println(a.getKmPago() + "-" + ax.getKmPago());
//		}*/
		totalizar();
	}
}