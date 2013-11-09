package motor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MensagemRMC {
	private static double NOS = 1.852;
	private String mensagem;
	private double lat;
	private double lng;
	private Date dataHora;
	private double velocidade;

	public MensagemRMC(String mensagem) {
		this.mensagem = mensagem;
		String[] array = mensagem.split(",");
		
		double graus = Double.parseDouble(array[3].substring(0,  2));
		double minutos = Double.parseDouble(array[3].substring(2)) / 60;
		this.lat = (graus + minutos) * -1;

		graus = Double.parseDouble(array[5].substring(0,  3));
		minutos = Double.parseDouble(array[5].substring(3)) / 60;
		lng = (graus + minutos) * -1;

		try {
			this.dataHora = new SimpleDateFormat("ddMMyyHHmmss").parse(array[9].concat(array[1]));
		} catch (ParseException e) {
			// TODO
		}
		this.velocidade = Double.parseDouble(array[7]) * NOS;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}
	
	public Date getDataHora() {
		return dataHora;
	}

	public double getVelocidade() {
		return velocidade;
	}

	public String toString() {
		return "".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHora))
			.concat(", " + lat)
			.concat(", " + lng)
			.concat(", " + velocidade);
	}
}