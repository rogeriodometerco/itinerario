package facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.AnaliseViagem;
import modelo.ArquivoImagem;
import modelo.Atendido;
import modelo.Escola;
import modelo.EscolaRota;
import modelo.FechamentoRota;
import modelo.Pessoa;
import modelo.Rota;
import dao.FechamentoRotaDao;
import dto.FechamentoRotaReport;
import dto.NomeAssinatura;
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
	@EJB
	private ArquivoImagemFacade arquivoImagemFacade;
	
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
		// Recupera imagens do motorista e da rota.
		ArquivoImagem arquivoImagemRota = null;
		ArquivoImagem arquivoImagemMotorista = null;
		try {
			arquivoImagemRota = arquivoImagemFacade.recuperarImagemRota(fechamentoRota.getRota());
			arquivoImagemMotorista = arquivoImagemFacade.recuperarImagemMotorista(fechamentoRota.getMotorista());
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] imagemMotorista = null;
		byte[] imagemRota = null;
		if (arquivoImagemMotorista != null) {
			imagemMotorista = arquivoImagemMotorista.getConteudo();
		}
		if (arquivoImagemRota != null) {
			imagemRota = arquivoImagemRota.getConteudo();
		}
		// Nomes assinatura.
		List<NomeAssinatura> nomesAssinatura = new ArrayList<NomeAssinatura>();
		nomesAssinatura.add(new NomeAssinatura(fechamentoRota.getMotorista().getPessoa().getNome(), "Motorista"));
		nomesAssinatura.add(new NomeAssinatura(fechamentoRota.getRota().getResponsavel(), "Responsável pela rota"));
		nomesAssinatura.add(new NomeAssinatura("Cristiano", "Responsável pelo Transporte"));
		nomesAssinatura.add(new NomeAssinatura("Paulo Boch", "Chefe Geral"));
		return new FechamentoRotaReport(fechamentoRota, new ArrayList<Pessoa>(pessoas), 
				escolas, imagemMotorista, imagemRota, nomesAssinatura);		
	}
	
	public List<PreFechamentoRota> getPreFechamentosRota(Date dataInicial, Date dataFinal) 
			throws Exception {

		List<AnaliseViagem> analises = analiseViagemFacade
				.recuperarAnalisesViagemAFechar(dataInicial, dataFinal);

		return agruparAnalisesEmPreFechamentos(analises, dataInicial, dataFinal);

		/*
		List<PreFechamentoRota> lista = new ArrayList<PreFechamentoRota>();
		PreFechamentoRota f = null;
		for (Rota r: rotaFacade.listar()) {
			f = getPreFechamentoRota(r, dataInicial, dataFinal);
			if (f != null) {
				lista.add(f);
			}
		}
		return lista;
		*/
	}

	public List<PreFechamentoRota> getPreFechamentosRota(Rota rota, Date dataInicial, Date dataFinal)
			throws Exception {

		List<AnaliseViagem> analises = analiseViagemFacade
				.recuperarAnalisesViagemAFechar(dataInicial, dataFinal, rota);

		return agruparAnalisesEmPreFechamentos(analises, dataInicial, dataFinal);
		/*
		// Retorna pré-fechamento não nulo se houver alguma análise de viagem.
		if (analises.size() > 0) {
			f = new PreFechamentoRota();
			f.setRota(rota);
			f.setMotorista(analises.get(0).getMotorista()); // TODO Resolver inicialização do motorista.
			f.setDataInicial(dataInicial);
			f.setDataFinal(dataFinal);
			f.setAnalisesViagem(analises);
		}
		return f;
		*/
	}

	private List<PreFechamentoRota> agruparAnalisesEmPreFechamentos(List<AnaliseViagem> analises, 
			Date dataInicial, Date dataFinal) {

		List<PreFechamentoRota> preFechamentos = new ArrayList<PreFechamentoRota>();
		
		// Ordena lista por rota e motorista.
		Collections.sort(analises, new Comparator<AnaliseViagem>() {
			public int compare(AnaliseViagem o1, AnaliseViagem o2) {
				if (o1.getRota().getId().longValue() < o2.getRota().getId().longValue()) {
					return -1;
				} else if (o1.getRota().getId().longValue() > o2.getRota().getId().longValue()) {
					return 1;
				} else {					
					if (o1.getMotorista().getId().longValue() < o2.getMotorista().getId().longValue()) {
						return -1;
					} else if (o1.getMotorista().getId().longValue() > o2.getMotorista().getId().longValue()) {
						return 1;
					} else {	
						return 0;
					}
				}
			}
		});

		PreFechamentoRota preFechamentoRota = null;
		for (AnaliseViagem analise: analises) {
			if (preFechamentoRota == null 
					|| !analise.getRota().getId().equals(preFechamentoRota.getRota().getId())  
					|| !analise.getMotorista().getId().equals(preFechamentoRota.getMotorista().getId())) {
				preFechamentoRota = new PreFechamentoRota();
				preFechamentoRota.setRota(analise.getRota());
				preFechamentoRota.setMotorista(analise.getMotorista());
				preFechamentoRota.setDataInicial(dataInicial);
				preFechamentoRota.setDataFinal(dataFinal);
				preFechamentoRota.setAnalisesViagem(new ArrayList<AnaliseViagem>());
				preFechamentos.add(preFechamentoRota);
			}
			preFechamentoRota.getAnalisesViagem().add(analise);
		}
		return preFechamentos;
	}
	
	public FechamentoRota criarFechamentoRota(PreFechamentoRota preFechamentoRota) 
			throws Exception {
		return salvar(
				preFechamentoRota.getFechamentoRota());
	}

	/*
	public List<FechamentoRota> getFechamentosRota(Date dataInicial, Date dataFinal) 
			throws Exception {

		List<FechamentoRota> lista = new ArrayList<FechamentoRota>();
		for (Rota r: rotaFacade.listar()) {
			lista.add(getFechamentoRota(r, dataInicial, dataFinal));
		}
		return lista;
	}
	*/
	
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