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
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import dto.FechamentoRotaReport;
import dto.PreFechamentoRota;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import modelo.AnaliseViagem;
import modelo.FechamentoRota;
import modelo.PontoRota;
import modelo.Rota;
import teste.FichaKmRodado;
import util.JsfUtil;
import facade.FechamentoRotaFacade;

@ManagedBean
@ViewScoped
public class FechamentoRotaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private FechamentoRota fechamentoRota;
	private List<FechamentoRota> lista;
	private String estadoView;
	private Rota rotaPesquisa;
	private Date dataInicialPesquisa;
	private Date dataFinalPesquisa;
	private double total[];
	@EJB
	private FechamentoRotaFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 1);
		dataInicialPesquisa = new Date(c.getTimeInMillis());
		c.set(2013, 10, 30);
		dataFinalPesquisa = new Date(c.getTimeInMillis());
	}

	public FechamentoRota getfechamentoRota() {
		return fechamentoRota;
	}

	public void setFechamentoRota(FechamentoRota fechamentoRota) {
		this.fechamentoRota = fechamentoRota;
	}

	public void listar() { 
		try {
			if (dataInicialPesquisa != null && dataFinalPesquisa == null 
					|| dataInicialPesquisa == null && dataFinalPesquisa != null) {
				JsfUtil.addMsgErro("Informe o período completo");
			} else if (rotaPesquisa != null && dataInicialPesquisa != null && dataFinalPesquisa != null) {
				System.out.println("listar() 1");
				this.lista = facade.listarFechamentosRota(rotaPesquisa, dataInicialPesquisa, dataFinalPesquisa);
			} else if (dataInicialPesquisa != null && dataFinalPesquisa != null){
				System.out.println("listar() 2");
				this.lista = facade.listarFechamentosRota(dataInicialPesquisa, dataFinalPesquisa);
			} else if (rotaPesquisa != null) {
				System.out.println("listar() 3");
				this.lista = facade.listarFechamentosRota(rotaPesquisa);
			} else {
				System.out.println("listar() 4");
				JsfUtil.addMsgErro("Informe a rota ou o período");
			}
			totalizar();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<FechamentoRota> getLista() {
		if (lista == null) {
			//	listar();
		}
		return this.lista;
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
		System.out.println("iniciarExclusao() " + fechamentoRota.getRota().getCodigo());
		this.fechamentoRota = fechamentoRota;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			// Não exclui as análises de viagem.
			for (AnaliseViagem a: fechamentoRota.getAnalisesViagem()) {
				a.setFechamentoRota(null);
			}
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

	public Rota getRotaPesquisa() {
		return rotaPesquisa;
	}

	public void setRotaPesquisa(Rota rotaPesquisa) {
		this.rotaPesquisa = rotaPesquisa;
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

	private void totalizar() {
		total = new double[10];
		for (FechamentoRota f: lista) {
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

	public double[] getTotal() {
		return total;
	}

	public void gerarRelatorio(FechamentoRota fechamentoRota) {
		try {
			System.out.println("gerarRelatorio() 1"); 
			FechamentoRotaReport f = facade.getFechamentoRotaReport(fechamentoRota);
			//TODO retirar linha abaixo após testes.
			//multiplicarDados(f);
			//System.out.println("gerarRelatorio() 2 ");
			//System.out.println("getEscolasToString " + f.getEscolasToString());
			List<FechamentoRotaReport> lista = new ArrayList<FechamentoRotaReport>();
			lista.add(f);
			String arquivo = JsfUtil.getExternalContext().getRealPath("/resources/reports/fechamentoRota.jrxml");
			//JasperReport report = JasperCompileManager.compileReport("c:/ambdev/teste/fechamentoRota.jrxml");
			JasperReport report = JasperCompileManager.compileReport(arquivo);
			System.out.println("Compilou relatório");
			JasperPrint print = JasperFillManager
					.fillReport(report, null, new JRBeanCollectionDataSource(
							lista));

			HttpServletResponse response = (HttpServletResponse)FacesContext
					.getCurrentInstance().getExternalContext().getResponse();

			response.setContentType("application/pdf"); 
			//response.setHeader("Content-disposition", "attachment;filename=\"FichaKmRodado.pdf\"");  
			response.setHeader("Content-disposition", "inline");  
			ServletOutputStream responseStream = response.getOutputStream();  
			JasperExportManager.exportReportToPdfStream(print, responseStream);
			System.out.println("Exportou");
			responseStream.flush();
			responseStream.close();
			FacesContext.getCurrentInstance().responseComplete();

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Aumenta quantidade de dados a exibir para fazer testes.
	 */
	private void multiplicarDados(FechamentoRotaReport f) {
		if (f.getFechamentoRota().getAnalisesViagem().size() > 0) {
			while (f.getFechamentoRota().getAnalisesViagem().size() < 83 ) {
				f.getFechamentoRota().getAnalisesViagem().add(
						f.getFechamentoRota().getAnalisesViagem().get(0));
			}
		}
		if (f.getPessoas().size() > 0) {
			while (f.getPessoas().size() < 30 ) {
				f.getPessoas().add(
						f.getPessoas().get(0));
			}
		}
	}

}