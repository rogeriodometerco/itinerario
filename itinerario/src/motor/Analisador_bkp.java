package motor;

import java.util.ArrayList;
import java.util.List;

import modelo.PontoLinha;
import modelo.PosicaoVeiculo;

public class Analisador_bkp {
	private HistoricoDePosicao historico;
	private Trajeto trajeto;
	private List<List<PosicaoVeiculo>> sequenciasNoTrajeto = new ArrayList<List<PosicaoVeiculo>>();
	private List<List<PosicaoVeiculo>> sequenciasForaDoTrajeto = new ArrayList<List<PosicaoVeiculo>>();
	private List<List<PontoLinha>> sequenciasNaoTransitadas = new ArrayList<List<PontoLinha>>();
	private List<AnaliseDePosicao> analises;
	
	public Analisador_bkp(Trajeto trajeto, HistoricoDePosicao historico) {
		this.trajeto = trajeto;
		this.historico = historico;
		analisar();
		formarSequenciasDentroForaDoTrajeto();
		formarSequenciasNaoTransitadas();
	}
	
	private void analisar() {
		analises = new ArrayList<AnaliseDePosicao>();
		for (PosicaoVeiculo posicao: historico.getPosicoes()) {
			analises.add(new AnaliseDePosicao(this.trajeto, posicao));
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private void formarSequenciasDentroForaDoTrajeto() {
		boolean noTrajeto = false;
		boolean foraDoTrajeto = false;
		for (PosicaoVeiculo posicao: historico.getPosicoes()) {
			if (trajeto.estaNoTrajeto(posicao)) {
				if (noTrajeto) {
					this.continuaNoTrajeto(posicao);
				} else {
					this.abrirSequenciaNoTrajeto(posicao);
				}
				noTrajeto = true;
				foraDoTrajeto = false;
			} else {
				if (foraDoTrajeto) {
					this.continuaForaDoTrajeto(posicao);
				} else {
					this.abrirSequenciaForaDoTrajeto(posicao);
				}
				foraDoTrajeto = true;
				noTrajeto = false;
			}
		}
	}
	
	private void abrirSequenciaNoTrajeto(PosicaoVeiculo posicao) {
		List<PosicaoVeiculo> sequencia = new ArrayList<PosicaoVeiculo>();
		sequencia.add(posicao);
		sequenciasNoTrajeto.add(sequencia);
	}
	
	private void continuaNoTrajeto(PosicaoVeiculo posicao) {
		sequenciasNoTrajeto.get(sequenciasNoTrajeto.size()-1).add(posicao);
	}

	private void abrirSequenciaForaDoTrajeto(PosicaoVeiculo posicao) {
		List<PosicaoVeiculo> sequencia = new ArrayList<PosicaoVeiculo>();
		sequencia.add(posicao);
		sequenciasForaDoTrajeto.add(sequencia);
	}
	
	private void continuaForaDoTrajeto(PosicaoVeiculo posicao) {
		sequenciasForaDoTrajeto.get(sequenciasForaDoTrajeto.size()-1).add(posicao);
	}
	
	private void formarSequenciasNaoTransitadas() {
		List<PontoLinha[]> intervalos = new ArrayList<PontoLinha[]>();
		for (List<PosicaoVeiculo> sequencia: sequenciasNoTrajeto) {
			PosicaoVeiculo p1 = sequencia.get(0);
			PosicaoVeiculo p2 = sequencia.get(sequencia.size()-1);
			PontoLinha pl1 = trajeto.obterPontoMaisProximo(p1, 
					LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);
			PontoLinha pl2 = trajeto.obterPontoMaisProximo(p2, 
					LatLngUtil.DISTANCIA_LIMITE_DENTRO_DO_TRAJETO);
			PontoLinha[] intervalo = new PontoLinha[] {pl1, pl2};
			intervalos.add(intervalo);
		}
		
		for (PontoLinha pontoLinha: trajeto.getPontos()) {
			int sequenciaInicial;
			int sequenciaFinal;
			boolean transitado;
			for (PontoLinha[] intervalo: intervalos) {
				if (intervalo[0].getSequencia() <= intervalo[1].getSequencia()) {
					sequenciaInicial = intervalo[0].getSequencia();
					sequenciaFinal = intervalo[1].getSequencia();
				} else {
					sequenciaInicial = intervalo[1].getSequencia();
					sequenciaFinal = intervalo[0].getSequencia();
					
				}
				if (pontoLinha.getSequencia() >= sequenciaInicial && pontoLinha.getSequencia() <= sequenciaFinal) {
					transitado = true;
					break;
				}
			}
		}
	}
}
