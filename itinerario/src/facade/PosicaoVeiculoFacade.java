package facade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.PosicaoVeiculo;
import modelo.Veiculo;
import dao.PosicaoVeiculoDao;

@Stateless
public class PosicaoVeiculoFacade 
extends GenericCrudFacade<PosicaoVeiculo> {

	@EJB
	private PosicaoVeiculoDao dao;

	@Override
	protected PosicaoVeiculoDao getDao() {
		return dao;
	}


	public List<PosicaoVeiculo> recuperarPosicoes(
			Veiculo veiculo, Date dataHoraInicial, Date dataHoraFinal) throws Exception {

		return getDao().recuperar(veiculo, dataHoraInicial, dataHoraFinal);

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