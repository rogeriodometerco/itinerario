package facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.AnaliseViagem;
import modelo.FechamentoRota;
import modelo.Rota;
import dao.FechamentoRotaDao;

@Stateless
public class FechamentoRotaFacade 
extends GenericCrudFacade<FechamentoRota> {

	@EJB
	private FechamentoRotaDao dao;
	@EJB
	private RotaFacade rotaFacade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;

	@Override
	protected FechamentoRotaDao getDao() {
		return dao;
	}

	public List<FechamentoRota> getFechamentosRota(Date dataInicial, Date dataFinal) 
			throws Exception {

		List<FechamentoRota> lista = new ArrayList<FechamentoRota>();
		for (Rota r: rotaFacade.listar()) {
			System.out.println("getFechamentosRota(), rota " + r.getCodigo());
			lista.add(getFechamentoRota(r, dataInicial, dataFinal));
		}
		return lista;
	}

	/**
	 * Recupera fechamento para a rota. Não existindo, cria.
	 * @param rota
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	public FechamentoRota getFechamentoRota(Rota rota, Date dataInicial, Date dataFinal)
			throws Exception {
		// TODO Avaliar como evitar de criar registro indevido, 
		// no caso do usuário informar um período errado.

		FechamentoRota f = null;

		try {
			String sql = "select x"
					+ " from FechamentoRota as x" 
					+ " where x.rota = :rota"
					+ " and x.dataInicial >= :dataInicial"
					+ " and x.dataFinal <= :dataFinal";
			f = getEntityManager().createQuery(sql, FechamentoRota.class)
					.setParameter("rota", rota)
					.setParameter("dataInicial", dataInicial)
					.setParameter("dataFinal", dataFinal)
					.getSingleResult();
		} catch (NoResultException nre) {
			f = criar(rota, dataInicial, dataFinal);
		}

		// Carregar ou vincular as análises de viagem.
		if (f.getConcluido()) {
			f.getAnalisesViagem().size();
		} else {
			List<AnaliseViagem> analises = analiseViagemFacade
					.recuperarAnalisesViagem(dataInicial, dataFinal, rota);
			f.setAnalisesViagem(analises);
			for (AnaliseViagem a: analises) {
				a.setFechamentoRota(f);
			}
		}
		return f;
	}

	private FechamentoRota criar(Rota rota, Date dataInicial, Date dataFinal) 
			throws Exception {
		FechamentoRota entidade = new FechamentoRota();
		entidade.setRota(rota);
		entidade.setDataInicial(dataInicial);
		entidade.setDataFinal(dataFinal);
		entidade.setConcluido(false);
		return salvar(entidade);
	}
}