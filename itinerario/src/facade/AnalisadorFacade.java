package facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;
import modelo.Rota;
import motor.AnaliseDeViagem;
import motor.AnaliseDePosicao;
import motor.Viagem;

@Stateless 
public class AnalisadorFacade {
	@EJB
	private ProgramacaoRotaFacade programacaoFacade;
	@EJB
	private PosicaoVeiculoFacade posicaoFacade;
	@EJB
	private PontoRotaFacade pontoFacade;

	/**
	 * Gera as análises de movimentação de veículo numa data conforme 
	 * a programação de suas rotas. 
	 * @param data
	 */
	public void gerarAnalises(Date data) throws Exception {
		List<ProgramacaoRota> programacoes = programacaoFacade.recuperarProgramacoes(data);
		for (ProgramacaoRota p: programacoes) {
			if (p.getVeiculo() != null) {
				// Monta os parâmetros para recuperar as posições do veículo.

				// Calendar para manipular o parâmetro <data>.
				Calendar calData = Calendar.getInstance();
				calData.setTime(data);
				
				// Data e hora inicial.
				Calendar c1 = Calendar.getInstance();
				c1.setTime(p.getHoraInicial());
				c1.set(calData.get(Calendar.YEAR), calData.get(Calendar.MONTH), calData.get(Calendar.DATE));

				// Data e hora final.
				Calendar c2 = Calendar.getInstance();
				c2.setTime(p.getHoraFinal());
				c2.set(calData.get(Calendar.YEAR), calData.get(Calendar.MONTH), calData.get(Calendar.DATE));
				
				// Recupera as posições do veículo.
				List<PosicaoVeiculo> posicoes = posicaoFacade
						.recuperarPosicoes(p.getVeiculo(), c1.getTime(), c2.getTime());
				
				//
				criarAnalises(p, new Viagem(posicoes));
				
			}
		}
	}
	
	private void criarAnalises(ProgramacaoRota programacao, Viagem viagem) {
		AnaliseDeViagem analisador = new AnaliseDeViagem(programacao, viagem);
	}
}
