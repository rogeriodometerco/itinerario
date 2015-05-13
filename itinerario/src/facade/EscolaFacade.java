package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import util.Paginador;

import modelo.Escola;
import modelo.EscolaRota;
import modelo.Passageiro;
import dao.EscolaDao;

@Stateless
public class EscolaFacade extends GenericCrudFacade<Escola> {

	@EJB
	private EscolaDao escolaDao;
	@EJB
	private EscolaRotaFacade escolaRotaFacade;
	
	@Override
	protected EscolaDao getDao() {
		return escolaDao;
	}

	public List<Escola> autocomplete(String chave) 
			throws Exception {
		String sql = "select x from Escola x where" +
				" (upper(x.codigo) like :chave" +
				" or upper(x.nome) like :chave" +
				" or upper(x.localidade) like :chave)"; 
		return getEntityManager()
				.createQuery(sql, Escola.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.getResultList();
	}
	
	public List<Escola> autocomplete(String chave, Paginador paginador) 
			throws Exception {
		String sql = "select x from Escola x where" +
				" (upper(x.codigo) like :chave" +
				" or upper(x.nome) like :chave" +
				" or upper(x.localidade) like :chave)"; 
		return getEntityManager()
				.createQuery(sql, Escola.class)
				.setParameter("chave", "%" + chave.toUpperCase() + "%")
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Escola> listarPorNomeContendo(String parteDoNome) 
			throws Exception {
		TypedQuery<Escola> query = getEntityManager().createQuery(
				"select x from Escola x where " +
						"upper(x.nome) like :chave", Escola.class);
		query.setParameter("chave", "%" + parteDoNome.toUpperCase() + "%");
		return query.getResultList();
	}
	
	public Escola recuperarPorCodigo(String codigo) 
			throws Exception {
		String sql = "select x from Escola as x"
				+ " where upper(x.codigo) = :codigo";
		try {
			return (Escola)getEntityManager().createQuery(sql)
					.setParameter("codigo", codigo.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}


	@Override
	protected void validar(Escola entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getCodigo() == null || entidade.getCodigo().trim().equals("")) {
			erros.add("Informe o código");
		}
		Escola e = recuperarPorCodigo(entidade.getCodigo());
		if (e != null && e.getCodigo().toUpperCase().equals(entidade.getCodigo().toUpperCase()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(e.getId()))) {
				erros.add("Já existe uma escola com o código " + entidade.getCodigo());
			}
		}
		if (entidade.getNome() == null || entidade.getNome().trim().equals("")) {
			erros.add("Informe o nome da escola");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void validarExclusao(Escola entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		List<EscolaRota> escolas = escolaRotaFacade.listar(entidade);
		if (escolas != null && escolas.size() > 0) {
			erros.add("Não é possível excluir a escola porque ela está vinculada a uma ou mais rotas");
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}
