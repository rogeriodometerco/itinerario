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

			return (AnaliseViagem) getEntityManager().createQuery(sql)
					.setParameter("programacao", programacao)
					.setParameter("dataViagem", dataViagem, TemporalType.DATE)
					.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("não achou nenhuma análise de viagem");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem) 
			throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal";

		return (List<AnaliseViagem>) getEntityManager().createQuery(sql)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem, 
			Veiculo veiculo) throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.veiculo = :veiculo";

		return (List<AnaliseViagem>) getEntityManager().createQuery(sql)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, Date dataFinalViagem, Rota rota) 
			throws Exception {

		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.rota = :rota";

		return (List<AnaliseViagem>) getEntityManager().createQuery(sql)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.setParameter("rota", rota)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AnaliseViagem> recuperarAnalisesViagem(Date dataInicialViagem, 
			Date dataFinalViagem, Rota rota, Veiculo veiculo) throws Exception {
		String sql = "select x"
				+ " from AnaliseViagem as x" 
				+ " where x.dataViagem between :dataInicial and :dataFinal"
				+ " and x.rota = :rota"
				+ " and x.veiculo = :veiculo";

		return (List<AnaliseViagem>) getEntityManager().createQuery(sql)
				.setParameter("dataInicial", dataInicialViagem, TemporalType.DATE)
				.setParameter("dataFinal", dataFinalViagem, TemporalType.DATE)
				.setParameter("rota", rota)
				.setParameter("veiculo", veiculo)
				.getResultList();
	}

}