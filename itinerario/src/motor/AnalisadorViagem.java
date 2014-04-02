package motor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.AnaliseParada;
import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.PontoRota;
import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;

public class AnalisadorViagem {
	private Date data;
	private ProgramacaoRota programacao;
	private Trajeto trajeto;
	private Viagem viagem;
	private AnaliseViagem analiseViagem;
	private List<AnalisadorPosicao> analisadoresPosicao;
	private List<AnalisadorParada> analisadoresParada;

	public AnalisadorViagem(Date data, ProgramacaoRota programacao, Viagem viagem) {
		this.data = data;
		this.programacao = programacao;
		this.trajeto = new Trajeto(programacao.getRota().getPontos());
		this.viagem = viagem;
	}

	public List<AnalisadorPosicao> getAnalisesDePosicao() {
		return analisadoresPosicao;
	}

	public List<AnalisadorParada> getAnalisadoresDeParada() {
		return analisadoresParada;
	}

	private double calcularKmNoTrajeto() {
		double distancia = 0;
		AnalisadorPosicao a1 = null;
		AnalisadorPosicao a2 = null;
		for (AnalisadorPosicao a: analisadoresPosicao) {
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
		// Arredonda para 2 casas decimais.
		return Math.round(distancia * 10) / 10;
	}

	private double calcularKmForaDoTrajeto() {
		double distancia = 0;
		AnalisadorPosicao a1 = null;
		AnalisadorPosicao a2 = null;
		for (AnalisadorPosicao a: analisadoresPosicao) {
			a1 = a2;
			a2 = a;
			if (a1 != null) {
				if (!a2.isNoTrajeto()) {
					distancia += MathUtil.calcularDistanciaEmKm(a1.getPosicaoVeiculo(), a2.getPosicaoVeiculo());
					System.out.println("AnalidadorViagem - fora do trajeto. ID: " + a2.getPosicaoVeiculo().getId() + " distancia: " + distancia);
					
				}
			}
		}
		// Arredonda para 2 casas decimais.
		return Math.round(distancia * 10) / 10;
	}

	private int calcularParadasCumpridas() {
		int paradasCumpridas = 0;
		for (AnalisadorParada analisador: this.analisadoresParada) {
			if (analisador.getParouNoPonto()) {
				paradasCumpridas++;
			}
		}
		return paradasCumpridas;
	}

	private int calcularParadasPrevistas() {
		return this.analisadoresParada.size();
	}

	private void criarAnalises() {
		// An�lises de posi��o.
		analisadoresPosicao = new ArrayList<AnalisadorPosicao>();
		for (PosicaoVeiculo posicao: viagem.getPosicoes()) {
			analisadoresPosicao.add(new AnalisadorPosicao(this.trajeto, posicao));
		}

		// An�lises de parada.
		analisadoresParada = new ArrayList<AnalisadorParada>();
		for (PontoRota ponto: trajeto.getPontosDeParada()) {
			analisadoresParada.add(new AnalisadorParada(this.viagem, ponto));
		}
	}

	public AnaliseViagem getAnaliseViagem() {
		if (analiseViagem == null) {
			criarAnalises();
			analiseViagem = new AnaliseViagem();
			//TODO Checar inicializa��o dos atributos.
			analiseViagem.setDataAnalise(new Date());
			analiseViagem.setDataViagem(data);
			analiseViagem.setProgramacao(programacao);
			analiseViagem.setKmForaTrajeto(calcularKmForaDoTrajeto());
			analiseViagem.setKmNoTrajeto(calcularKmNoTrajeto());
			analiseViagem.setKmPago(calcularKmNoTrajeto());
			analiseViagem.setKmPrevisto(programacao.getRota().getQuilometragem());
			// deixa 2 casas decimais.
			double kmRealizado = Math.round(viagem.getDistanciaPercorrida() * 10) / 10;
			analiseViagem.setKmRealizado(kmRealizado); 
			analiseViagem.setValorKm(programacao.getRota().getValorKm());
			double valorPago = (double)Math.round(analiseViagem.getKmPago() * analiseViagem.getValorKm() * 100) / 100;
			analiseViagem.setValorPago(valorPago);
			analiseViagem.setParadasCumpridas(calcularParadasCumpridas());
			analiseViagem.setParadasPrevistas(calcularParadasPrevistas());
			analiseViagem.setVeiculo(programacao.getVeiculo());
			analiseViagem.setRota(programacao.getRota());
			// Seta an�lises de posi��o.
			analiseViagem.setAnalisesPosicao(new ArrayList<AnalisePosicao>());
			for (AnalisadorPosicao analisadorPosicao: analisadoresPosicao) {
				analiseViagem.getAnalisesPosicao().add(
						new AnalisePosicao(analiseViagem, analisadorPosicao.getPosicaoVeiculo(), 
								analisadorPosicao.isNoTrajeto()));
			}
			// Seta an�lises de parada.
			analiseViagem.setAnalisesParada(new ArrayList<AnaliseParada>());
			for (AnalisadorParada analisadorParada: analisadoresParada) {
				analiseViagem.getAnalisesParada().add(
						new AnaliseParada(analiseViagem, analisadorParada.getPontoParada(), 
								analisadorParada.getParouNoPonto()));
			}
		}
		return analiseViagem;
	}

}

