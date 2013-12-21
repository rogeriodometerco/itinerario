package motor;

import java.util.List;

import modelo.PontoRota;
import modelo.PosicaoVeiculo;

public class Trajeto {
	private List<PontoRota> pontos;

	public Trajeto(List<PontoRota> pontos) {
		this.pontos = pontos;
	}
	
	/**
	 * Verifica se um registro de posi��o est� no trajeto de uma rota.
	 * @param posicao
	 * @return
	 */
	public boolean estaNoTrajeto(PosicaoVeiculo posicao) {
		PontoRota p = obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);

		if (p == null) { 
			//System.out.println("Posi��o fora do trajeto: " + posicao.getId()); 
		} else {
			//System.out.println("Posi��o no trajeto: " + posicao.getId()); 
		}
		return p != null; 
	}

	/**
	 * Retorna o ponto do trajeto mais pr�ximo a uma posi��o de ve�culo, desde que esteja
	 * dentro de um raio.
	 * @param posicao
	 * @param raio
	 * @return
	 */
	public PontoRota obterPontoMaisProximo(PosicaoVeiculo posicao, int raio) {
		PontoRota pontoMaisProximo = null;
		double menorDistancia = Double.POSITIVE_INFINITY;
		double distancia = 0;
		for (PontoRota ponto: pontos) {
			distancia = LatLngUtil.calcularDistancia(
					posicao.getLat(), posicao.getLng(), ponto.getLat(), ponto.getLng()); 
			if (distancia < menorDistancia && distancia <= raio) {
				pontoMaisProximo = ponto;
				menorDistancia = distancia;
				/*
				System.out.println("PONTO MAIS PR�XIMO DE "
						+ posicao.getId() + ": "
						+ pontoMaisProximo.getId() 
						+ " A " + menorDistancia + " METROS");
				*/
			}
		}
		/*		
		if (posicao.getId() == 1631) {
			System.out.println("PONTO MAIS PR�XIMO DE 1631: " + pontoMaisProximo.getId() 
					+ " A " + menorDistancia + " METROS");
		}
		 */
		return pontoMaisProximo;
	}
	

	public PontoRota getPontoDoTrajeto(PosicaoVeiculo posicao) {
		return obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);
	}

	public List<PontoRota> getPontos() {
		return pontos;
	}
}
