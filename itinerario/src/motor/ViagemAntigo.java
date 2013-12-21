package motor;

import java.util.List;

import modelo.PosicaoVeiculo;
 
public class ViagemAntigo {
	private List<PosicaoVeiculo> posicoes;
	
	public ViagemAntigo(List<PosicaoVeiculo> posicoes) {
		this.posicoes = posicoes;
	}
	
	public List<PosicaoVeiculo> getPosicoes() {
		return posicoes;
	}
	
	public double getDistanciaPercorrida() {
		double distancia = 0;
		PosicaoVeiculo p1 = null;
		PosicaoVeiculo p2 = null;
		for (PosicaoVeiculo p: posicoes) {
			if (p1 == null) {
				p1 = p;
			} else {
				if (p2 == null) {
					p2 = p;
				} else {
					p1 = p2;
					p2 = p;
				}
				distancia += MathUtil.calcularDistancia(p1, p2);
			}
		}
		return distancia;
	}
	
}
