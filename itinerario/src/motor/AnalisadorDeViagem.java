package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.AgendamentoLinha;
import modelo.EscalaVeiculo;
import modelo.Linha;
import modelo.PosicaoVeiculo;

public class AnalisadorDeViagem {
	private EscalaVeiculo escala;
	private Trajeto trajeto;
	private Viagem viagem;
	private List<AnaliseDePosicao> analises;
	private double distanciaNoTrajeto;
	private double distanciaForaDoTrajeto;
	
	public AnalisadorDeViagem(EscalaVeiculo escala, Viagem viagem) {
		this.escala = escala;
		this.trajeto = new Trajeto(escala.getAgendamento().getLinha().getPontos());
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
			if (a1 == null) {
				a1 = a;
			} else {
				if (a2 == null) {
					a2 = a;
				} else {
					a1 = a2;
					a2 = a;
				}
				if (a2.isNoTrajeto()) {
					distancia += MathUtil.calcularDistancia(a1.getPosicaoVeiculo(), a2.getPosicaoVeiculo());
				}
			}
		}
		distanciaNoTrajeto = distancia;
	}
	
	private void calcularDistanciaForaDoTrajeto() {
		double distancia = 0;
		AnaliseDePosicao a1 = null;
		AnaliseDePosicao a2 = null;
		for (AnaliseDePosicao a: analises) {
			if (a1 == null) {
				a1 = a;
			} else {
				if (a2 == null) {
					a2 = a;
				} else {
					a1 = a2;
					a2 = a;
				}
				if (!a2.isNoTrajeto()) {
					distancia += MathUtil.calcularDistancia(a1.getPosicaoVeiculo(), a2.getPosicaoVeiculo());
				}
			}
		}
		distanciaForaDoTrajeto = distancia;
	}
	
	public Linha getLinha() {
		return escala.getAgendamento().getLinha();
	}
	
	public AgendamentoLinha getAgendamento() {
		return escala.getAgendamento();
	}
	
	public EscalaVeiculo getEscala() {
		return escala;
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
	
