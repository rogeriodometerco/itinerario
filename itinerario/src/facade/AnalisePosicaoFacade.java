package facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.AnalisePosicao;
import modelo.AnaliseViagem;
import modelo.PosicaoVeiculo;
import dao.AnalisePosicaoDao;


@Stateless
public class AnalisePosicaoFacade 
extends GenericCrudFacade<AnalisePosicao> {

	@EJB
	private AnalisePosicaoDao dao;

	@Override
	protected AnalisePosicaoDao getDao() {
		return dao;
	}

	/**
	 * Retorna a análise para uma posição de veículo se existir.
	 * @param analiseViagem
	 * @param posicaoVeiculo
	 * @return
	 * @throws Exception
	 */
	public AnalisePosicao recuperarAnalisePosicao(AnaliseViagem analiseViagem, 
			PosicaoVeiculo posicaoVeiculo) throws Exception{
		try {
			String sql = "select x"
					+ " from AnalisePosicao as x" 
					+ " where x.analiseViagem = :analiseViagem"
					+ " and x.posicaoVeiculo = :posicaoVeiculo";

			return (AnalisePosicao) getEntityManager().createQuery(sql)
					.setParameter("analiseViagem", analiseViagem)
					.setParameter("posicaoVeiculo", posicaoVeiculo)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<AnalisePosicao> recuperarAnalisesPosicao(AnaliseViagem analiseViagem) 
			throws Exception{
		// TODO avaliar
		String sql = "select x"
					+ " from AnalisePosicao as x" 
					+ " where x.analiseViagem = :analiseViagem";
		
			return (List<AnalisePosicao>) getEntityManager().createQuery(sql)
					.setParameter("analiseViagem", analiseViagem)
					.getResultList();
	}

}