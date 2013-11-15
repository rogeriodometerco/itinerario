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
		PontoLinha p = obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);

		if (p == null) { 
			//System.out.println("Posição fora do trajeto: " + posicao.getId()); 
		} else {
			//System.out.println("Posição no trajeto: " + posicao.getId()); 
		}
		return p != null; 
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
				menorDistancia = distancia;
				/*
				System.out.println("PONTO MAIS PRÓXIMO DE "
						+ posicao.getId() + ": "
						+ pontoMaisProximo.getId() 
						+ " A " + menorDistancia + " METROS");
				*/
			}
		}
		/*		
		if (posicao.getId() == 1631) {
			System.out.println("PONTO MAIS PRÓXIMO DE 1631: " + pontoMaisProximo.getId() 
					+ " A " + menorDistancia + " METROS");
		}
		 */
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
