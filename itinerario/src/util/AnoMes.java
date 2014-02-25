package util;

import java.util.Calendar;
import java.util.Date;

public class AnoMes {
	private int ano;
	private int mes;

	public AnoMes() {
		this.ano = Calendar.getInstance().get(Calendar.YEAR);
		this.mes = Calendar.getInstance().get(Calendar.MONTH);
	}
	
	public AnoMes(int ano, int mes) {
		this.ano = ano;
		this.mes = mes;
	}
	
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}
	
	public Date getDataInicial() {
		Calendar c = Calendar.getInstance();
		// Primeiro dia do mês.
		c.set(ano, mes, 1);
		return new Date(c.getTimeInMillis());
	}
	
	public Date getDataFinal() {
		Calendar c = Calendar.getInstance();
		// Último dia do mês.
		c.set(ano, mes, c.getMaximum(Calendar.DAY_OF_MONTH));
		return new Date(c.getTimeInMillis());
	}

}
