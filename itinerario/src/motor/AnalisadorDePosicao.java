package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.AgendaLinha;
import modelo.EscalaVeiculo;
import modelo.Linha;
import modelo.PosicaoVeiculo;

public class AnalisadorDePosicao {
	private EscalaVeiculo escala;
	private Trajeto trajeto;
	private HistoricoDePosicao historico;
	private List<AnaliseDePosicao> analises;
	private double distanciaNoTrajeto;
	private double distanciaForaDoTrajeto;
	
	public AnalisadorDePosicao(EscalaVeiculo escala, HistoricoDePosicao historico) {
		this.escala = escala;
		this.trajeto = new Trajeto(escala.getAgenda().getLinha().getPontos());
		this.historico = historico;
		analisar();
		calcularDistanciaNoTrajeto();
		calcularDistanciaForaDoTrajeto();
	}
	
	private void analisar() {
		analises = new ArrayList<AnaliseDePosicao>();
		for (PosicaoVeiculo posicao: historico.getPosicoes()) {
			analises.add(new AnaliseDePosicao(this.trajeto, posicao));
		}
	}
	
	public List<AnaliseDePosicao> getAnalises() {
		return analises;
	}
	
	public double getDistanciaTotalPercorrida() {
		return historico.getDistanciaPercorrida();
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
		return escala.getAgenda().getLinha();
	}
	
	public AgendaLinha getAgenda() {
		return escala.getAgenda();
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
	
