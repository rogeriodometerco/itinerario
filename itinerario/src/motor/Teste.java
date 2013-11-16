package motor;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import modelo.AgendamentoLinha;

public class Teste {
	public static void main(String[] args) {
		System.out.println("Domingo: "+ Calendar.SUNDAY);
		
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		AgendamentoLinha agenda = new AgendamentoLinha();
		
		//Calendar a = Calendar.getInstance();
		Calendar a = new GregorianCalendar(new Locale("pt", "br"));
		a.set(2013, 0, 15);
		System.out.println(s.format(a.getTime()));

		//a.setTime(new Date());//data maior
		//a.add(Calendar.MILLISECOND, 4);
		a.setTimeInMillis(0);
		agenda.setHoraInicial(a.getTime());

		System.out.println(s.format(a.getTime()));

		System.out.println(agenda.getHoraInicial().getTime());
		System.out.println(s.format(agenda.getHoraInicial().getTime()));
		
		
		Calendar b = Calendar.getInstance();  
		b.set(2013, 9, 19);// data menor
		
		System.out.println(s.format(b.getTime()));
		System.out.println((a.getTimeInMillis()-b.getTimeInMillis())/1000/3600/24);

	}
}
