package motor;

import modelo.PosicaoVeiculo;

public class AnaliseDePosicao {
	private Trajeto trajeto;
	private PosicaoVeiculo posicaoVeiculo;
	private boolean noTrajeto;
	
	public AnaliseDePosicao(Trajeto trajeto, PosicaoVeiculo posicao) {
		this.trajeto = trajeto;
		this.posicaoVeiculo = posicao;
		this.noTrajeto = trajeto.estaNoTrajeto(posicao);
	}

	public PosicaoVeiculo getPosicaoVeiculo() {
		return posicaoVeiculo;
	}

	public boolean isNoTrajeto() {
		return noTrajeto;
	}
	
	
}
