package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Calendario;
import modelo.EscolaRota;
import modelo.ProgramacaoRota;
import modelo.Rota;
import dao.RotaDao;

@Stateless
public class RotaFacade
	extends GenericCrudFacade<Rota> {
	
	@EJB
	private RotaDao dao;
	@EJB
	private ProgramacaoRotaFacade programacaoRotaFacade;
	@EJB
	private EscolaRotaFacade escolaRotaFacade;
	
	@Override
	protected RotaDao getDao() {
		return dao;
	}
	
	public Rota recuperarParaEdicao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}
	 
	public Rota recuperarParaExclusao(Long id) throws Exception {
		return recuperarParaEdicaoOuExclusao(id);
	}

	private Rota recuperarParaEdicaoOuExclusao(Long id) throws Exception {
		String sql = "select x"
				+ " from Rota x "
				+ " left join fetch x.pontos " 
				+ " where x.id = :id";
		return getEntityManager().createQuery(sql, Rota.class)
				.setParameter("id", id)
				.getSingleResult();

	}
	
	public List<Rota> autocomplete(String chave) 
			throws Exception {
		TypedQuery<Rota> query = getEntityManager().createQuery(
				"select x from Rota x where (" +
						"upper(x.codigo) like :chave" +
						" or upper(x.nome) like :chave" +
						" or upper(x.origem) like :chave" +
						" or upper(x.destino) like :chave" +
				")", Rota.class);
		query.setParameter("chave", "%" + chave.toUpperCase() + "%");
		return query.getResultList();
	}
	 
	public Rota recuperarPorCodigo(String codigo) 
			throws Exception {
		String sql = "select r from Rota as r"
				+ " where upper(r.codigo) = :codigo";
		try {
			return (Rota)getEntityManager().createQuery(sql)
					.setParameter("codigo", codigo.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	@Override
	protected void validar(Rota entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getCodigo() == null || entidade.getCodigo().trim().equals("")) {
			erros.add("Informe o c�digo");
		}
		Rota r = recuperarPorCodigo(entidade.getCodigo());
		if (r != null && r.getCodigo().toUpperCase().equals(entidade.getCodigo().toUpperCase()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(r.getId()))) {
				erros.add("J� existe uma rota com o c�digo " + entidade.getCodigo());
			}
		}
		if (entidade.getNome() == null || entidade.getNome().trim().equals("")) {
			erros.add("Informe o nome da rota");
		}
		if (entidade.getPontos() == null || entidade.getPontos().size() == 0) {
			erros.add("Informe os pontos da rota");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void validarExclusao(Rota entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		List<ProgramacaoRota> programacoes = programacaoRotaFacade.listar(entidade);
		if (programacoes != null && programacoes.size() > 0) {
			erros.add("N�o � poss�vel excluir a rota porque ela possui programa��o de ve�culo");
		}
		List<EscolaRota> escolas = escolaRotaFacade.listar(entidade);
		if (escolas != null && escolas.size() > 0) {
			erros.add("N�o � poss�vel excluir a rota porque ela possui escola vinculada");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}
}
