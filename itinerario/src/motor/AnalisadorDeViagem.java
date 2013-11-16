package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.AgendamentoLinha;
import modelo.EscalaVeiculo;
import modelo.Linha;
import modelo.PosicaoVeiculo;
import modelo.ProgramacaoLinha;

public class AnalisadorDeViagem {
	private ProgramacaoLinha programacao;
	private Trajeto trajeto;
	private Viagem viagem;
	private List<AnaliseDePosicao> analises;
	private double distanciaNoTrajeto;
	private double distanciaForaDoTrajeto;
	
	public AnalisadorDeViagem(ProgramacaoLinha programacao, Viagem viagem) {
		this.programacao = programacao;
		this.trajeto = new Trajeto(programacao.getLinha().getPontos());
		this.viagem = viagem;
		analisar();
		calcularDistanciaNoTrajeto();
		calcularDistanciaForaDoTrajeto();
	}
	
	private void analisar() {
		analises = new ArrayList<AnaliseDePosicao>();
		for (PosicaoVeiculo posicao: viagem.getPosicoes()) {
			analises.add(new AnaliseDePosicao(this.trajeto, posicao));
		}
	}
	
	public List<AnaliseDePosicao> getAnalises() {
		return analises;
	}
	
	public double getDistanciaTotalPercorrida() {
		return viagem.getDistanciaPercorrida();
	}
	
	private void calcularDistanciaNoTrajeto() {
		double distancia = 0;
		AnaliseDePosicao a1 = null;
		AnaliseDePosicao a2 = null;
		for (AnaliseDePosicao a: analises) {
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
		for (AnaliseDePosicao a: analises) {
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
	
	public Linha getLinha() {
		return programacao.getLinha();
	}
	
	public ProgramacaoLinha getProgramacao() {
		return programacao;
	}
	
	public double getDistanciaNoTrajeto() {
		return distanciaNoTrajeto;
	}
	
	public double getDistanciaForaDoTrajeto() {
		return distanciaForaDoTrajeto;
	}

	public double getDiferencaDeDistancia() {
		return getDistanciaNoTrajeto() - getLinha().getQuilometragem();
	}
}
	
