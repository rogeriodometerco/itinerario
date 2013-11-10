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
import motor.AnalisadorDeViagem;
import motor.Viagem;
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
	 * Recupera os registros de posi��o de um ve�culo que devem ser analisados em rela��o
	 * a sua escala num determinado dia.
	 * @param escala
	 * @return
	 */
	private List<PosicaoVeiculo> recuperarPosicoesParaAnalise(
			EscalaVeiculo escala, Date data) {

		// TODO Refactoring. N�o est� legal.
		// O par�metro data est� atrelado ao agendamento cuja escala est�
		// associada, e a origem tem que se preocupar com isso. 
		
		// Objeto auxiliar.
		Calendar paramData = Calendar.getInstance();
		paramData.setTime(data);

		// Data e hora inicial.
		Calendar c1 = Calendar.getInstance();
		c1.setTime(escala.getAgendamento().getHoraInicial());
		c1.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

		// Data e hora final.
		Calendar c2 = Calendar.getInstance();
		c2.setTime(escala.getAgendamento().getHoraFinal());
		c2.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

		return getDao().recuperar(escala.getVeiculo(), c1.getTime(), c2.getTime());

	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public List<AnalisadorDeViagem> analisarPosicoesDeCadaEscala(Date data) 
			throws Exception {

		List<AnalisadorDeViagem> listaRetorno = new ArrayList<AnalisadorDeViagem>();
		List<PosicaoVeiculo> posicoes = null;
		List<PontoLinha> pontos = null;
		for (EscalaVeiculo escala: escalaFacade.recuperarEscalas(data)) {
			posicoes = this.recuperarPosicoesParaAnalise(escala, data);
			System.out.println(posicoes.size() + "posi��es recuperadas");
			// TODO Recuperar entidades relacionadas j� na leitura do banco.
			escala.getAgendamento().getLinha().getPontos().size();
			pontos = pontoLinhaFacade.recuperarPontos(escala.getAgendamento().getLinha()); 
			AnalisadorDeViagem analisador = new AnalisadorDeViagem(escala, new Viagem(posicoes));
			listaRetorno.add(analisador);
		}
		return listaRetorno;
	}
}
