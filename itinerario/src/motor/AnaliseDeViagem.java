package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.PontoRota;
import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;
import modelo.Rota;

public class AnaliseDeViagem {
	private ProgramacaoRota programacao;
	private Trajeto trajeto;
	private Viagem viagem;
	private List<AnaliseDePosicao> analisesDePosicao;
	private List<AnaliseDeParada> analisesDeParada;
	private List<PontoRota> pontosDoTrajetoVisitados;
	private double distanciaNoTrajeto;
	private double distanciaForaDoTrajeto;
	
	public AnaliseDeViagem(ProgramacaoRota programacao, Viagem viagem) {
		this.programacao = programacao;
		this.trajeto = new Trajeto(programacao.getRota().getPontos());
		this.viagem = viagem;
		analisar();
		calcularDistanciaNoTrajeto();
		calcularDistanciaForaDoTrajeto();
	}
	
	private void analisar() {
		// Análises de posição.
		analisesDePosicao = new ArrayList<AnaliseDePosicao>();
		for (PosicaoVeiculo posicao: viagem.getPosicoes()) {
			analisesDePosicao.add(new AnaliseDePosicao(this.trajeto, posicao));
		}

		// Análises de parada.
		analisesDeParada = new ArrayList<AnaliseDeParada>();
		for (PontoRota ponto: trajeto.getPontosDeParada()) {
			analisesDeParada.add(new AnaliseDeParada(this.viagem, ponto));
		}
}
	
	public List<AnaliseDePosicao> getAnalisesDePosicao() {
		return analisesDePosicao;
	}
	
	public List<AnaliseDeParada> getAnalisesDeParada() {
		return analisesDeParada;
	}
	
	public double getDistanciaTotalPercorrida() {
		return viagem.getDistanciaPercorrida();
	}
	
	private void calcularDistanciaNoTrajeto() {
		double distancia = 0;
		AnaliseDePosicao a1 = null;
		AnaliseDePosicao a2 = null;
		for (AnaliseDePosicao a: analisesDePosicao) {
			a1 = a2;
			a2 = a;
			if (a1 != null) {
				if (a2.isNoTrajeto()) {
					distancia += MathUtil.calcularDistancia(
							a1.getPosicaoVeiculo(), 
							a2.getPosicaoVeiculo());
				}
			}
		}
		distanciaNoTrajeto = Math.round(distancia * 10) / 10;
	}
	
	private void calcularDistanciaForaDoTrajeto() {
		double distancia = 0;
		AnaliseDePosicao a1 = null;
		AnaliseDePosicao a2 = null;
		for (AnaliseDePosicao a: analisesDePosicao) {
			a1 = a2;
			a2 = a;
			if (a1 != null) {
				if (!a2.isNoTrajeto()) {
					System.out.println("Posição fora do trajeto");
					distancia += MathUtil.calcularDistancia(a1.getPosicaoVeiculo(), a2.getPosicaoVeiculo());
				}
			}
		}
		distanciaForaDoTrajeto = Math.round(distancia * 10) / 10;
	}
	
	public Rota getRota() {
		return programacao.getRota();
	}
	
	public ProgramacaoRota getProgramacao() {
		return programacao;
	}
	
	public double getDistanciaNoTrajeto() {
		return distanciaNoTrajeto;
	}
	
	public double getDistanciaForaDoTrajeto() {
		return distanciaForaDoTrajeto;
	}

	public double getDiferencaDeDistancia() {
		return getDistanciaNoTrajeto() - getRota().getQuilometragem();
	}
}
	
