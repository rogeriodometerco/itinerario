package managedbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import modelo.Calendario;
import modelo.Escola;
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
			System.out.println("Relat�rio gerado.");
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