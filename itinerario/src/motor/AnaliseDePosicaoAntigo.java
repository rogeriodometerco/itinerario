package motor;

import modelo.PosicaoVeiculo;

public class AnaliseDePosicaoAntigo {
	private TrajetoAntigo trajetoAntigo;
	private PosicaoVeiculo posicaoVeiculo;
	private boolean noTrajeto;
	
	public AnaliseDePosicaoAntigo(TrajetoAntigo trajetoAntigo, PosicaoVeiculo posicao) {
		this.trajetoAntigo = trajetoAntigo;
		this.posicaoVeiculo = posicao;
		this.noTrajeto = trajetoAntigo.estaNoTrajeto(posicao);
		//System.out.println("Nova análise: " + posicao.getId() + " no trajeto: " + noTrajeto);
	}

	public PosicaoVeiculo getPosicaoVeiculo() {
		return posicaoVeiculo;
	}

	public boolean isNoTrajeto() {
		return noTrajeto;
	}
	
	
}
