package teste;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import facade.PontoRotaFacade;

import modelo.Calendario;
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

public class Teste {
	public static void main(String[] args) {

		//reportCalendario();
		//testarDate();
		reportFichaKmRodado();
		//reportRotaEPontos();

	}

	private static void reportRotaEPontos() {
		try {
			Rota rota1 = new Rota();
			rota1.setNome("Rota1");
			
			Rota rota2 = new Rota();
			rota2.setNome("Rota2");
			
			PontoRota pontoRota1 = new PontoRota();
			pontoRota1.setRota(rota1);
			pontoRota1.setDescricao("Ponto 1 rota 1");
			
			PontoRota pontoRota2 = new PontoRota();
			pontoRota2.setRota(rota1);
			pontoRota2.setDescricao("Ponto 2 rota 1");
			
			PontoRota pontoRota3 = new PontoRota();
			pontoRota3.setRota(rota2);
			pontoRota3.setDescricao("Ponto 3 rota 2");
			
			PontoRota pontoRota4 = new PontoRota();
			pontoRota4.setRota(rota2);
			pontoRota4.setDescricao("Ponto 4 rota 2");
			
			List<PontoRota> pontos = new ArrayList<PontoRota>();
			pontos.add(pontoRota1);
			pontos.add(pontoRota2);
			pontos.add(pontoRota3);
			pontos.add(pontoRota4);

			JasperReport report = JasperCompileManager.compileReport("c:/ambdev/teste/RotaPontos.jrxml");
			System.out.println("Compilou");

			JasperPrint print = JasperFillManager
					.fillReport(report, null, new JRBeanCollectionDataSource(pontos));

			JasperExportManager.exportReportToHtmlFile(print, "c:/ambdev/teste/RotaPontos.html");  
			JasperExportManager.exportReportToPdfFile(print, "c:/ambdev/teste/RotaPontos.pdf");  
			JasperExportManager.exportReportToXmlFile(print, "c:/ambdev/teste/RotaPontos.xml", false);
			System.out.println("Relatório gerado.");
			JasperViewer.viewReport(print, true);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void reportFichaKmRodado() {
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

//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("pontos", r.getPontos());
			JasperReport report = JasperCompileManager.compileReport("c:/ambdev/teste/FichaKmRodado.jrxml");
			System.out.println("Compilou");
			JasperPrint print = JasperFillManager
					.fillReport(report, null, new JRBeanCollectionDataSource(l));

			JasperExportManager.exportReportToHtmlFile(print, "c:/ambdev/teste/FichaKmRodado.html");  
			JasperExportManager.exportReportToPdfFile(print, "c:/ambdev/teste/FichaKmRodado.pdf");  
			JasperExportManager.exportReportToXmlFile(print, "c:/ambdev/teste/FichaKmRodado.xml", false);
			System.out.println("Relatório gerado.");
			JasperViewer.viewReport(print, true);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static void testarDate() {
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

	private static void reportCalendario() {
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
	}
}
