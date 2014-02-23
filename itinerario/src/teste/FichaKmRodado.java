package teste;

import java.util.List;

import modelo.PontoRota;
import modelo.Rota;

public class FichaKmRodado {
	private Rota rota;
	private List<PontoRota> pontos;
	private String obs;
	
	public Rota getRota() {
		return rota;
	}
	
	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public List<PontoRota> getPontos() {
		return pontos;
	}

	public void setPontos(List<PontoRota> pontos) {
		this.pontos = pontos;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
}
