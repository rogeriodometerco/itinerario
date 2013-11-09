package motor;

import java.util.List;

import modelo.PontoLinha;
import modelo.PosicaoVeiculo;

public class Trajeto {
	private List<PontoLinha> pontos;

	public Trajeto(List<PontoLinha> pontos) {
		this.pontos = pontos;
	}
	
	/**
	 * Verifica se um registro de posição está no trajeto de uma linha.
	 * @param posicao
	 * @return
	 */
	public boolean estaNoTrajeto(PosicaoVeiculo posicao) {
		return obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO) != null;
	}

	/**
	 * Retorna o ponto do trajeto mais próximo a uma posição de veículo, desde que esteja
	 * dentro de um raio.
	 * @param posicao
	 * @param raio
	 * @return
	 */
	public PontoLinha obterPontoMaisProximo(PosicaoVeiculo posicao, int raio) {
		PontoLinha pontoMaisProximo = null;
		double menorDistancia = Double.POSITIVE_INFINITY;
		double distancia = 0;
		for (PontoLinha ponto: pontos) {
			distancia = LatLngUtil.calcularDistancia(
					posicao.getLat(), posicao.getLng(), ponto.getLat(), ponto.getLng()); 
			if (distancia < menorDistancia && distancia <= raio) {
				pontoMaisProximo = ponto;
			}
		}
		return pontoMaisProximo;
	}
	

	public PontoLinha getPontoDoTrajeto(PosicaoVeiculo posicao) {
		return obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);
	}

	public List<PontoLinha> getPontos() {
		return pontos;
	}
}
