package facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.PosicaoVeiculo;
import modelo.ProgramacaoLinha;
import modelo.ProgramacaoRota;
import modelo.Veiculo;
import motor.AnalisadorDeViagemAntigo;
import motor.ViagemAntigo;
import dao.PosicaoVeiculoDao;

@Stateless
public class PosicaoVeiculoFacade 
extends GenericCrudFacade<PosicaoVeiculo> {

	@EJB
	private PosicaoVeiculoDao dao;
	@EJB
	private ProgramacaoLinhaFacade programacaoFacade;
	@EJB
	private PontoLinhaFacade pontoLinhaFacade;

	@Override
	protected PosicaoVeiculoDao getDao() {
		return dao;
	}

	/**
	 * Recupera os registros de posição de um veículo que devem ser analisados em relação
	 * a sua escala num determinado dia.
	 * @param programacao
	 * @return
	 */
	// TODO eliminar este método assim que não estiver mais sendo utilizado.
	@Deprecated
	private List<PosicaoVeiculo> recuperarPosicoesParaAnalise(
			ProgramacaoLinha programacao, Date dataInicial, Date dataFinal) {

		// TODO Refactoring. Não está legal.
		// O parâmetro data está atrelada a programação, e a origem tem que se preocupar com isso. 

		// Objeto auxiliar.
		Calendar paramData = Calendar.getInstance();
		paramData.setTime(dataInicial);

		// Data e hora inicial.
		Calendar c1 = Calendar.getInstance();
		c1.setTime(programacao.getHoraInicial());
		c1.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

		// Data e hora final.
		Calendar c2 = Calendar.getInstance();
		c2.setTime(programacao.getHoraFinal());
		c2.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

		return getDao().recuperar(programacao.getVeiculo(), c1.getTime(), c2.getTime());

		/*
		
		List<PosicaoVeiculo> posicoes = new ArrayList<PosicaoVeiculo>();

		while (!paramData.after(dataFinal)) {

			if (paramData.get(Calendar.DAY_OF_WEEK) == programacao.getDiaSemana()) {
				System.out.println("paramData.get(Calendar.DAY_OF_WEEK) == programacao.getDiaSemana() " 
						+ programacao.getDiaSemana() + " - id da programacao: " + programacao.getId());
				// Data e hora inicial.
				Calendar c1 = Calendar.getInstance();
				c1.setTime(programacao.getHoraInicial());
				c1.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

				// Data e hora final.
				Calendar c2 = Calendar.getInstance();
				c2.setTime(programacao.getHoraFinal());
				c2.set(paramData.get(Calendar.YEAR), paramData.get(Calendar.MONTH), paramData.get(Calendar.DATE));

				posicoes.addAll(getDao().recuperar(programacao.getVeiculo(), c1.getTime(), c2.getTime()));
			}
			paramData.add(Calendar.DAY_OF_MONTH, 1);
		}
		return posicoes;
		*/
	}


	public List<PosicaoVeiculo> recuperarPosicoes(
			Veiculo veiculo, Date dataHoraInicial, Date dataHoraFinal) throws Exception {

		return getDao().recuperar(veiculo, dataHoraInicial, dataHoraFinal);

	}

	/**
	 * 
	 * @param dataInicial
	 * @return
	 */
	// TODO Eliminar este método assim que não estiver mais sendo utilizado.
	@Deprecated
	public List<AnalisadorDeViagemAntigo> analisarPosicoesDeCadaProgramacao(Date dataInicial, Date dataFinal,
			Veiculo veiculo) throws Exception {

		List<AnalisadorDeViagemAntigo> listaRetorno = new ArrayList<AnalisadorDeViagemAntigo>();
		List<PosicaoVeiculo> posicoes = null;
		for (ProgramacaoLinha programacao: programacaoFacade.recuperarProgramacoes(dataInicial, dataFinal)) {
			posicoes = this.recuperarPosicoesParaAnalise(programacao, dataInicial, dataFinal);
			System.out.println(posicoes.size() + "posições recuperadas");
			// TODO Recuperar entidades relacionadas já na leitura do banco.
			programacao.getLinha().getPontos().size();
			AnalisadorDeViagemAntigo analisador = new AnalisadorDeViagemAntigo(programacao, new ViagemAntigo(posicoes));
			listaRetorno.add(analisador);
		}
		return listaRetorno;
	}

	public void salvar(List<PosicaoVeiculo> posicoes) throws Exception {
		for (PosicaoVeiculo posicao: posicoes) {
			if (recuperarPosicao(posicao.getVeiculo(), posicao.getDataHora()) == null) {
				salvar(posicao);
			}
		}
	}

	public PosicaoVeiculo recuperarPosicao(Veiculo veiculo, Date dataHora) 
			throws Exception {
		String sql = "select p from PosicaoVeiculo as p"
				+ " where p.veiculo = :veiculo"
				+ " and p.dataHora = :dataHora";

		try {
			return (PosicaoVeiculo)getEntityManager().createQuery(sql)
					.setParameter("veiculo", veiculo)
					.setParameter("dataHora", dataHora)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	/**
	 * Recupera as posições de um veículo numa data.
	 * @param veiculo
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PosicaoVeiculo> recuperarPosicoes(Veiculo veiculo, Date data) 
			throws Exception {
		Calendar aux = Calendar.getInstance();
		aux.setTime(data);
		aux.add(Calendar.DAY_OF_MONTH, 1);
		String sql = "select p from PosicaoVeiculo as p"
				+ " where p.veiculo = :veiculo"
				+ " and p.dataHora >= :dataInicial"
				+ " and p.dataHora < :dataFinal"
				+ " order by p.dataHora";

		return (List<PosicaoVeiculo>)getEntityManager().createQuery(sql)
				.setParameter("veiculo", veiculo)
				.setParameter("dataInicial", data)
				.setParameter("dataFinal", aux.getTime())
				.getResultList();
	}


	@SuppressWarnings("unchecked")
	public List<PosicaoVeiculo> recuperarUltimaPosicaoDeCadaVeiculo() 
			throws Exception {

		String sql = "select ultima from PosicaoVeiculo ultima"
				+ " where (veiculo, dataHora) in ("
				+ "select p.veiculo, max(p.dataHora) from PosicaoVeiculo p"
				+ " group by p.veiculo)"
				+ " order by ultima.veiculo.identificacao";
		return (List<PosicaoVeiculo>)getEntityManager().createQuery(sql).getResultList();
		/*

		List<PosicaoVeiculo> posicoes = new ArrayList<PosicaoVeiculo>();

		String sql = "select p.veiculo, max(p.dataHora) from PosicaoVeiculo p"
			+ " group by p.veiculo"
		List<Object[]> lista = getEntityManager().createQuery(sql).getResultList();
		for (Object[] o: lista) {
			posicoes.add(recuperarPosicao((Veiculo)o[0], (Date)o[1]);
		}
		if (posicoes.size() == 0) {
			posicoes = null;
		}
		return posicoes;

		 */

	}

	public PosicaoVeiculo recuperarUltimaPosicao(Veiculo veiculo) throws Exception {
		PosicaoVeiculo posicao = null;
		String sql = "select max(p.dataHora) from PosicaoVeiculo p where p.veiculo = :veiculo";
		@SuppressWarnings("unchecked")
		List<Object> data = getEntityManager().createQuery(sql)
		.setParameter("veiculo", veiculo).getResultList();
		// TODO Ver se apenas um teste é suficiente.
		if (data != null && data.size() > 0) {
			posicao = recuperarPosicao(veiculo, (Date)data.get(0));
		}
		return posicao;
	}


}