package facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.AnaliseViagem;
import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;
import motor.AnalisadorViagem;
import motor.Viagem;

@Stateless 
public class AnalisadorFacade {
	@EJB
	private ProgramacaoRotaFacade programacaoFacade;
	@EJB
	private PosicaoVeiculoFacade posicaoFacade;
	@EJB
	private PontoRotaFacade pontoFacade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;
	
	/**
	 * Gera as an�lises de movimenta��o de ve�culo numa data conforme 
	 * a programa��o de suas rotas. 
	 * @param data
	 */
	public List<AnaliseViagem> analisarViagens(Date data) throws Exception {
		List<AnaliseViagem> analises = new ArrayList<AnaliseViagem>();
		List<ProgramacaoRota> programacoes = programacaoFacade.recuperarProgramacoes(data);
		System.out.println("analisadorFacade recuperou " + programacoes.size());
		for (ProgramacaoRota p: programacoes) {
			if (p.getVeiculo() != null) {
				// Monta os par�metros para recuperar as posi��es do ve�culo.

				// Calendar para manipular o par�metro <data>.
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
				
				// Recupera as posi��es do ve�culo.
				List<PosicaoVeiculo> posicoes = posicaoFacade
						.recuperarPosicoes(p.getVeiculo(), c1.getTime(), c2.getTime());
				
				// Recupera a an�lise de viagem se j� existir.
				AnaliseViagem analiseViagem = analiseViagemFacade.recuperarAnaliseViagem(p, data);
				// S� faz a an�lise se ela n�o est� associada a um fechamento.
				if (analiseViagem == null || analiseViagem.getFechamentoRota() == null) {
					// Se a an�lise existe, remove e cria novamente.
					if (analiseViagem != null) {
						analiseViagemFacade.excluir(analiseViagem);
					}
					analises.add(criarAnalise(data, p, new Viagem(posicoes)));
				}
			}
		}
		return analises;
	}
	
	private AnaliseViagem criarAnalise(Date data, ProgramacaoRota programacao, Viagem viagem) 
			throws Exception {
		AnalisadorViagem analisador = new AnalisadorViagem(data, programacao, viagem);
		return analiseViagemFacade.salvar(analisador.getAnaliseViagem());
	}
}
