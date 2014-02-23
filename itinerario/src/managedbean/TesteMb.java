package managedbean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import modelo.Calendario;
import modelo.Escola;
import modelo.PontoRota;
import modelo.Rota;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import teste.FichaKmRodado;
import util.JsfUtil;
import facade.TesteFacade;

@ManagedBean
@ViewScoped
public class TesteMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private Escola escola;
	@EJB
	private TesteFacade facade;
	private List<Escola> escolas;

	public void testar() {
		try {
			escolas = new ArrayList<Escola>();
			for (int i = 0; i < 100; i++) {
				escola = new Escola();
				escola.setNome("Escola teste " + ++i);
				escolas.add(escola);
			}
			facade.salvar(escolas);
		} catch (Exception e) {
			JsfUtil.addMsgErro("getLocalizedMessage(): " + e.getLocalizedMessage());
			JsfUtil.addMsgErro("getMessage(): " + e.getMessage());
			JsfUtil.addMsgErro("getCause(): " + e.getCause());
		}

	}


    public void downloadFile() {
        FileInputStream in = null;
        String filename = "FichaKmRodado.pdf";
        File file = new File("c:/ambdev/teste/FichaKmRodado.pdf");
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\""); 
            response.setContentLength((int) file.length()); 
            response.setContentType("application/pdf");
            in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            JsfUtil.addMsgSucesso(file.length() + " bytes do arquivo " + file.getName() + " " + file.getAbsolutePath());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
 
    }
    
	public void PDF() throws JRException, IOException{
		try {
			Rota r = new Rota();
			r.setNome("Rota 000001");
			r.setPontos(new ArrayList<PontoRota>());
			PontoRota pr = new PontoRota();
			pr.setId(999l);
			pr.setDescricao("Descrição do ponto da rota");
			r.getPontos().add(pr);

			pr = new PontoRota();
			pr.setId(1000l);
			pr.setDescricao("Descrição do ponto 1000 da rota");
			r.getPontos().add(pr);

			FichaKmRodado ficha = new FichaKmRodado();
			ficha.setRota(r);

			List<FichaKmRodado> l = new ArrayList<FichaKmRodado>();
			l.add(ficha);


			JasperReport report = JasperCompileManager.compileReport("c:/ambdev/teste/FichaKmRodado.jrxml");
			System.out.println("Compilou");
			JasperPrint print = JasperFillManager
					.fillReport(report, null, new JRBeanCollectionDataSource(l));

			//JasperExportManager.exportReportToPdfFile(print, "c:/ambdev/teste/FichaKmRodado.pdf");  

			HttpServletResponse response = (HttpServletResponse)FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			
			response.setContentType("application/pdf"); 
			//response.setHeader("Content-disposition", "attachment;filename=\"FichaKmRodado.pdf\"");  
			response.setHeader("Content-disposition", "inline");  
			ServletOutputStream responseStream = response.getOutputStream();  
			JasperExportManager.exportReportToPdfStream(print, responseStream);
			//JasperRunManager.runReportToPdf(getClass().getResourceAsStream("c:/ambdev/teste/FichaKmRodado.pdf"), );
			System.out.println("Exportou");
			responseStream.flush();
			responseStream.close();
			//FacesContext.getCurrentInstance().renderResponse();
			FacesContext.getCurrentInstance().responseComplete();
			
/*			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			JasperReport pathReport = JasperCompileManager.compileReport("c:/ambdev/teste/FichaKmRodado.jrxml");
			System.out.println("Compilou");
			JasperPrint print = JasperFillManager.fillReport(pathReport, null, new JRBeanCollectionDataSource(l));
			baos.write(JasperExportManager.exportReportToPdf(print));

			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());

			OutputStream out = response.getOutputStream();
			context.responseComplete();
			baos.writeTo(out);
			out.flush();
			out.close();
			baos.close();
			System.out.println("Exportou");
	*/		
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}  

	public void testarArquivo() {
		try {
			List<Calendario> lista = new ArrayList<Calendario>();
			Calendario c = new Calendario();
			c.setId(1l);
			c.setNome("Calendario 01");
			lista.add(c);

			c = new Calendario();
			c.setId(2l);
			c.setNome("Calendario 02");
			lista.add(c);

			JasperReport report = JasperCompileManager.compileReport("c:/ambdev/teste/teste02.jrxml");
			System.out.println("Compilou");
			JasperPrint print = JasperFillManager
					.fillReport(report, null, new JRBeanCollectionDataSource(lista));
			JasperExportManager.exportReportToHtmlFile(print, "c:/ambdev/teste/RelatorioCalendarios.html");  
			JasperExportManager.exportReportToPdfFile(print, "c:/ambdev/teste/RelatorioCalendarios.pdf");  
			JasperExportManager.exportReportToXmlFile(print, "c:/ambdev/teste/RelatorioCalendarios.xml", false);
			System.out.println("Relatório gerado.");
			JasperViewer.viewReport(print, true);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Domingo: "+ Calendar.SUNDAY);

		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		//Calendar a = Calendar.getInstance();
		Calendar a = new GregorianCalendar(new Locale("pt", "br"));
		a.set(2013, 0, 15);
		System.out.println(s.format(a.getTime()));

		//a.setTime(new Date());//data maior
		//a.add(Calendar.MILLISECOND, 4);
		a.setTimeInMillis(0);

		System.out.println(s.format(a.getTime()));



		Calendar b = Calendar.getInstance();  
		b.set(2013, 9, 19);// data menor

		System.out.println(s.format(b.getTime()));
		System.out.println((a.getTimeInMillis()-b.getTimeInMillis())/1000/3600/24);

	}

}