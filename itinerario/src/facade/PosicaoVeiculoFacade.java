package facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.EscalaVeiculo;
import modelo.PontoLinha;
import modelo.PosicaoVeiculo;
import motor.AnalisadorDePosicao;
import motor.HistoricoDePosicao;
import motor.Trajeto;
import dao.PosicaoVeiculoDao;

@Stateless
public class PosicaoVeiculoFacade 
	extends GenericCrudFacade<PosicaoVeiculo> {

	@EJB
	private PosicaoVeiculoDao dao;
	@EJB
	private EscalaVeiculoFacade escalaFacade;
	@EJB
	private PontoLinhaFacade pontoLinhaFacade;
	
	@Override
	protected PosicaoVeiculoDao getDao() {
		return dao;
	}

	/**
	 * Recupera os registros de posição de um veículo que devem ser analisados em relação
	 * a sua escala num determinado dia.
	 * @param escala
	 * @return
	 */
	private List<PosicaoVeiculo> recuperarPosicoesParaAnalise(
			EscalaVeiculo escala, Date data) {
		
		// Objeto auxiliar.
		Calendar param = Calendar.getInstance();
		param.setTime(data);
		
		// Data e hora inicial.
		Calendar c1 = Calendar.getInstance();
		c1.setTime(escala.getAgenda().getHoraInicial());
		c1.set(param.get(Calendar.YEAR), param.get(Calendar.MONTH), param.get(Calendar.DATE));

		// Data e hora final.
		Calendar c2 = Calendar.getInstance();
		c2.setTime(escala.getAgenda().getHoraFinal());
		c2.set(param.get(Calendar.YEAR), param.get(Calendar.MONTH), param.get(Calendar.DATE));
		
		return getDao().recuperar(escala.getVeiculo(), c1.getTime(), c2.getTime());
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public List<AnalisadorDePosicao> analisarPosicoesDeCadaEscala(Date data) 
		throws Exception {
		
		List<AnalisadorDePosicao> listaRetorno = new ArrayList<AnalisadorDePosicao>();
		List<PontoLinha> pontos = null;
		List<PosicaoVeiculo> posicoes = null;
		for (EscalaVeiculo escala: escalaFacade.recuperarEscalas(data)) {
			posicoes = this.recuperarPosicoesParaAnalise(escala, data);
			System.out.println(posicoes.size() + "posições recuperadas");
			// TODO Recuperar entidades relacionadas já na leitura do banco.
			escala.getAgenda().getLinha().getPontos().size();
			pontos = pontoLinhaFacade.recuperarPontos(escala.getAgenda().getLinha()); 
			AnalisadorDePosicao analisador = new AnalisadorDePosicao(escala, new HistoricoDePosicao(posicoes));
			listaRetorno.add(analisador);
		}
		return listaRetorno;
	}
}
