package motor;

import modelo.PontoRota;

public class AnaliseDeParada {
	private Viagem viagem;
	private PontoRota pontoParada;
	private boolean parouNoPonto;
	
	public AnaliseDeParada(Viagem viagem, PontoRota pontoParada) {
		this.viagem = viagem;
		this.pontoParada = pontoParada;
		this.parouNoPonto = viagem.parouEm(pontoParada);
	}

	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public PontoRota getPontoParada() {
		return pontoParada;
	}

	public void setPontoParada(PontoRota pontoParada) {
		this.pontoParada = pontoParada;
	}

	public boolean getParouNoPonto() {
		return parouNoPonto;
	}

	public void setParouNoPonto(boolean parouNoPonto) {
		this.parouNoPonto = parouNoPonto;
	}

	
}
