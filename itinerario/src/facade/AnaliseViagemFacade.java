package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import modelo.AnaliseViagem;
import modelo.ProgramacaoRota;
import modelo.Rota;
import modelo.Veiculo;
import dao.AnaliseViagemDao;

@Stateless
public class AnaliseViagemFacade 
extends GenericCrudFacade<AnaliseViagem> {

	@EJB
	private AnaliseViagemDao dao;

	@Override
	protected AnaliseViagemDao getDao() {
		return dao;
	}

	public AnaliseViagem recuperarAnaliseViagem(ProgramacaoRota programacao, Date dataViagem)
			throws Exception {
		try {
			String sql = "select x"
					+ " from AnaliseViagem as x" 
					+ " where x.programacao = :programacao"
					+ " and x.dataViagem = :dataViagem";
			return (AnaliseViagem) getEntityManager().createQuery(sql, AnaliseViagem.class)
					.setParameter("programacao", programacao)
					.setParameter("dataViagem", dataViagem, TemporalType.DATE)
					.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("não achou nenhuma análise de viagem");
			return null;
		}
	}

	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem) 
			throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal";
		return (List<AnaliseViagem>) getEntityManager().createQuery(sql, AnaliseViagem.class)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.getResultList();
	}

	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem, 
			Veiculo veiculo) throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.veiculo = :veiculo";
		return (List<AnaliseViagem>) getEntityManager().createQuery(sql, AnaliseViagem.class)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem, Rota rota) 
			throws Exception {

		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.rota = :rota";
		return (List<AnaliseViagem>) getEntityManager().createQuery(sql, AnaliseViagem.class)
				.setParameter("dataInicial", dataInicialViagem)
				.setParameter("dataFinal", dataFinalViagem)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<AnaliseViagem> recuperarAnalisesViagemAFechar(Date dataInicialViagem, Date dataFinalViagem, Rota rota) 
			throws Exception {

		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.rota = :rota"
				+ " and x.fechamentoRota is null";
		return (List<AnaliseViagem>) getEntityManager().createQuery(sql, AnaliseViagem.class)
				.setParameter("dataInicial", dataInicialViagem)
				.setParameter("dataFinal", dataFinalViagem)
				.setParameter("rota", rota)
				.getResultList();
	}
	
	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, 
			Date dataFinalViagem, Rota rota, Veiculo veiculo) throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.rota = :rota"
				+ " and x.veiculo = :veiculo";
		return (List<AnaliseViagem>) getEntityManager().createQuery(sql, AnaliseViagem.class)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.setParameter("rota", rota)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

}