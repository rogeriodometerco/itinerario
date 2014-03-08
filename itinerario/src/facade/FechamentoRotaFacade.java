package facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.AnaliseViagem;
import modelo.Atendido;
import modelo.Escola;
import modelo.EscolaRota;
import modelo.FechamentoRota;
import modelo.Pessoa;
import modelo.Rota;

import java.util.List;
import dao.FechamentoRotaDao;
import dto.FechamentoRotaReport;
import dto.PreFechamentoRota;

@Stateless
public class FechamentoRotaFacade 
extends GenericCrudFacade<FechamentoRota> {

	@EJB
	private FechamentoRotaDao dao;
	@EJB
	private RotaFacade rotaFacade;
	@EJB
	private AnaliseViagemFacade analiseViagemFacade;
	@EJB
	private EscolaRotaFacade escolaRotaFacade;
	@EJB
	private AtendidoFacade atendidoFacade;

	@Override
	protected FechamentoRotaDao getDao() {
		return dao;
	}

	public FechamentoRotaReport getFechamentoRotaReport(FechamentoRota fechamentoRota) 
			throws Exception {
		
		// Monta lista das escolas atendidas.
		List<Escola> escolas = new ArrayList<Escola>();
		for (EscolaRota er: escolaRotaFacade.listar(fechamentoRota.getRota())) {
			escolas.add(er.getEscola());
		}
		// Monta lista das pessoas atendidas. Coloca num Set para não repetir a pessoa.
		Collection<Pessoa> pessoas = new HashSet<Pessoa>();
		for (Atendido a: atendidoFacade.listar(fechamentoRota.getRota())) {
			pessoas.add(a.getPassageiro().getPessoa());
		}
		
		return new FechamentoRotaReport(fechamentoRota, new ArrayList<Pessoa>(pessoas), escolas);		
	}
	
	public List<PreFechamentoRota> getPreFechamentosRota(Date dataInicial, Date dataFinal) 
			throws Exception {

		List<PreFechamentoRota> lista = new ArrayList<PreFechamentoRota>();
		PreFechamentoRota f = null;
		for (Rota r: rotaFacade.listar()) {
			f = getPreFechamentoRota(r, dataInicial, dataFinal);
			if (f != null) {
				lista.add(f);
			}
		}
		return lista;
	}

	public PreFechamentoRota getPreFechamentoRota(Rota rota, Date dataInicial, Date dataFinal)
			throws Exception {

		PreFechamentoRota f = null;
		List<AnaliseViagem> analises = analiseViagemFacade
				.recuperarAnalisesViagemAFechar(dataInicial, dataFinal, rota);

		// Retorna pré-fechamento não nulo se houver alguma análise de viagem.
		if (analises.size() > 0) {
			f = new PreFechamentoRota();
			f.setRota(rota);
			f.setDataInicial(dataInicial);
			f.setDataFinal(dataFinal);
			f.setAnalisesViagem(analises);
		}
		return f;
	}

	public FechamentoRota criarFechamentoRota(PreFechamentoRota preFechamentoRota) 
			throws Exception {
		return salvar(
				preFechamentoRota.getFechamentoRota());
	}

	public List<FechamentoRota> getFechamentosRota(Date dataInicial, Date dataFinal) 
			throws Exception {

		List<FechamentoRota> lista = new ArrayList<FechamentoRota>();
		for (Rota r: rotaFacade.listar()) {
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

	public List<FechamentoRota> listarFechamentosRota(Date dataInicial, Date dataFinal)
			throws Exception {

		String sql = "select distinct x"
				+ " from FechamentoRota as x " 
				+ " left join fetch x.analisesViagem " 
				+ " where " 
				+ " x.dataInicial <= :dataFinal"
				+ " and x.dataFinal >= :dataInicial";
		return getEntityManager().createQuery(sql, FechamentoRota.class)
				.setParameter("dataInicial", dataInicial)
				.setParameter("dataFinal", dataFinal)
				.getResultList();
	}

	public List<FechamentoRota> listarFechamentosRota(Rota rota, Date dataInicial, Date dataFinal)
			throws Exception {

		String sql = "select distinct x"
				+ " from FechamentoRota as x " 
				+ " left join fetch x.analisesViagem " 
				+ " where " 
				+ " x.rota = :rota"
				+ " and x.dataInicial <= :dataFinal"
				+ " and x.dataFinal >= :dataInicial";
		return getEntityManager().createQuery(sql, FechamentoRota.class)
				.setParameter("rota", rota)
				.setParameter("dataInicial", dataInicial)
				.setParameter("dataFinal", dataFinal)
				.getResultList();
	}

	public List<FechamentoRota> listarFechamentosRota(Rota rota)
			throws Exception {

		String sql = "select distinct x"
				+ " from FechamentoRota as x " 
				+ " left join fetch x.analisesViagem " 
				+ " where " 
				+ " x.rota = :rota";
		return getEntityManager().createQuery(sql, FechamentoRota.class)
				.setParameter("rota", rota)
				.getResultList();
	}

}