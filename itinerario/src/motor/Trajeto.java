package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.PontoRota;
import modelo.PosicaoVeiculo;

public class Trajeto {
	private List<PontoRota> pontos;

	public Trajeto(List<PontoRota> pontos) {
		this.pontos = pontos;
	}
	
	/**
	 * Verifica se um registro de posição está no trajeto de uma rota.
	 * @param posicao
	 * @return
	 */
	public boolean estaNoTrajeto(PosicaoVeiculo posicao) {
		PontoRota p = obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);

		if (posicao.getVeiculo().getId() == 9104) {
			if (p == null) {
				System.out.println("Trajeto.estaNoTrajeto() - Posição fora do trajeto. ID: " + posicao.getId());
			} else {
				//System.out.println("Trajeto.estaNoTrajeto() - Posição no trajeto. ID: " + posicao.getId() +  ". Ponto ID: " + p.getId());
				
			}
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
			}
		}
		return pontoMaisProximo;
	}
	

	public PontoRota getPontoDoTrajeto(PosicaoVeiculo posicao) {
		return obterPontoMaisProximo(posicao, 
				LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);
	}

	public List<PontoRota> getPontos() {
		return pontos;
	}
	
	/**
	 * Retorna os pontos do trajeto onde o veículo deve parar.
	 * @return
	 */
	public List<PontoRota> getPontosDeParada() {
		List<PontoRota> lista = new ArrayList<PontoRota>();
		for (PontoRota p: pontos) {
			if (p.getParada()) {
				lista.add(p);
			}
		}
		if (lista.size() == 0) {
			lista = null;
		}
		return lista;
	}
}
