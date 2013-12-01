package motor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Teste {
	public static void main(String[] args) {
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
