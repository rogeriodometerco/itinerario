package motor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.AnaliseViagem;
import modelo.PontoRota;
import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;
import modelo.Rota;

public class AnalisadorViagem {
	private ProgramacaoRota programacao;
	private Trajeto trajeto;
	private Viagem viagem;
	private List<AnalisadorPosicao> analisesDePosicao;
	private List<AnalisadorParada> analisadoresDeParada;
	//private List<PontoRota> pontosDoTrajetoVisitados;
	//private double distanciaNoTrajeto;
	//private double distanciaForaDoTrajeto;
	
	public AnalisadorViagem(ProgramacaoRota programacao, Viagem viagem) {
		this.programacao = programacao;
		this.trajeto = new Trajeto(programacao.getRota().getPontos());
		this.viagem = viagem;
		analisar();
		//calcularDistanciaNoTrajeto();
		//calcularDistanciaForaDoTrajeto();
	}
	
	private void analisar() {
		// Análises de posição.
		analisesDePosicao = new ArrayList<AnalisadorPosicao>();
		for (PosicaoVeiculo posicao: viagem.getPosicoes()) {
			analisesDePosicao.add(new AnalisadorPosicao(this.trajeto, posicao));
		}

		// Análises de parada.
		analisadoresDeParada = new ArrayList<AnalisadorParada>();
		for (PontoRota ponto: trajeto.getPontosDeParada()) {
			analisadoresDeParada.add(new AnalisadorParada(this.viagem, ponto));
		}
}
	
	public List<AnalisadorPosicao> getAnalisesDePosicao() {
		return analisesDePosicao;
	}
	
	public List<AnalisadorParada> getAnalisadoresDeParada() {
		return analisadoresDeParada;
	}
	/*
	public double getDistanciaTotalPercorrida() {
		return viagem.getDistanciaPercorrida();
	}
	*/
	
	private double calcularDistanciaNoTrajeto() {
		double distancia = 0;
		AnalisadorPosicao a1 = null;
		AnalisadorPosicao a2 = null;
		for (AnalisadorPosicao a: analisesDePosicao) {
			a1 = a2;
			a2 = a;
			if (a1 != null) {
				if (a2.isNoTrajeto()) {
					distancia += MathUtil.calcularDistanciaEmKm(
							a1.getPosicaoVeiculo(), 
							a2.getPosicaoVeiculo());
				}
			}
		}
		//distanciaNoTrajeto = Math.round(distancia * 10) / 10;
		return Math.round(distancia * 10) / 10;
	}
	
	private double calcularDistanciaForaDoTrajeto() {
		double distancia = 0;
		AnalisadorPosicao a1 = null;
		AnalisadorPosicao a2 = null;
		for (AnalisadorPosicao a: analisesDePosicao) {
			a1 = a2;
			a2 = a;
			if (a1 != null) {
				if (!a2.isNoTrajeto()) {
					System.out.println("Posição fora do trajeto");
					distancia += MathUtil.calcularDistanciaEmKm(a1.getPosicaoVeiculo(), a2.getPosicaoVeiculo());
				}
			}
		}
		//distanciaForaDoTrajeto = Math.round(distancia * 10) / 10;
		return Math.round(distancia * 10) / 10;
	}
	
	public Rota getRota() {
		return programacao.getRota();
	}
	
	public ProgramacaoRota getProgramacao() {
		return programacao;
	}
	/*
	public double getDistanciaNoTrajeto() {
		return distanciaNoTrajeto;
	}
	
	public double getDistanciaForaDoTrajeto() {
		return distanciaForaDoTrajeto;
	}

	public double getDiferencaDeDistancia() {
		return getDistanciaNoTrajeto() - getRota().getQuilometragem();
	}
*/	
	public AnaliseViagem getAnaliseViagem() {
		AnaliseViagem analise = new AnaliseViagem();
		 //TODO Inicialização dos atributos.
		analise.setDataAnalise(new Date());
		analise.setDataViagem(viagem.getDataReferencia());
		analise.setProgramacao(programacao);
		analise.setKmCumprida(1.0);
		analise.setKmForaTrajeto(calcularDistanciaForaDoTrajeto());
		analise.setKmNoTrajeto(calcularDistanciaNoTrajeto());
		analise.setKmPago(1.0);
		analise.setKmPrevisto(programacao.getRota().getQuilometragem());
		analise.setKmRealizado(viagem.getDistanciaPercorrida()/1000);
		analise.setValorKm(1.0);
		analise.setParadasCumpridas(calcularParadasCumpridas());
		analise.setParadasPrevistas(calcularParadasPrevistas());
		return analise;
	}
	
	private int calcularParadasCumpridas() {
		int paradasCumpridas = 0;
		for (AnalisadorParada analisador: this.analisadoresDeParada) {
			if (analisador.getParouNoPonto()) {
				paradasCumpridas++;
			}
		}
		return paradasCumpridas;
	}
	
	private int calcularParadasPrevistas() {
		return this.analisadoresDeParada.size();
	}
}
	
