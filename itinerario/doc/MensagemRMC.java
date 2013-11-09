public class MensagemRMC {

	private String mensagem;
	private double lat;
	private double lng;
	private Date dataHora;
	private double velocidade;

	public MensagemRMC(String mensagem) {
		this.mensagem = mensagem;
		String[] array = mensagem.split(",");
		this.lat = Integer.parseInt(array[3].substr(0, 2))
			+ (Double.parseDouble(array[3].substr(2)) / 60) * 100;
		this.lng = Integer.parseInt(array[5].substr(0, 3))
			+ (Double.parseDouble(array[5].substr(3)) / 60) * 100;
		this.dataHora = new SimpleDateFormat("ddMMyyHHmmss").parse(array[9].concat(array[1]));
		this.velocidade = Double.parseDouble(array[7]) * 1.852;
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

	public Date getVelocidade() {
		return velocidade;
	}

	public String toString() {
		return "".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data))
			.concat(", " + lat)
			.concat(", " + lng)
			.concat(", " + velocidade);
	}
}