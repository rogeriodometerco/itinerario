package util;

import java.util.List;

import modelo.PontoLinha;
import modelo.PosicaoVeiculo;

public class LinhaHelper {

		public void analisar(List<PosicaoVeiculo> posicoes) {
			for (PosicaoVeiculo posicao : posicoes) {
				if (estaNaLinha(posicao)) {
					
				}
			}
		}
		
		public boolean estaNaLinha(PosicaoVeiculo posicao) {
			return true;
		}
		
		public List<PontoLinha> getPontosNoIntervalo(PosicaoVeiculo p1, PosicaoVeiculo p2) {
			return null;
		}
		
		/**
		 * Dado um registro de posição, retorna o ponto da linha mais próximo.
		 * @param p
		 * @return
		 */
		public PontoLinha obterPontoMaisProximo(PosicaoVeiculo p) {
			return null;
		}
		
		
}
