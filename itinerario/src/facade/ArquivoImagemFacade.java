package facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.ArquivoImagem;
import modelo.Motorista;
import modelo.Rota;
import modelo.Veiculo;
import dao.ArquivoImagemDao;

@Stateless
public class ArquivoImagemFacade extends GenericCrudFacade<ArquivoImagem> {

	@EJB
	private ArquivoImagemDao arquivoImagemDao;
	@EJB
	private EscolaRotaFacade escolaRotaFacade;
	
	@Override
	protected ArquivoImagemDao getDao() {
		return arquivoImagemDao;
	}

	public ArquivoImagem recuperarImagemMotorista(Motorista motorista) throws Exception {
		ArquivoImagem imagem = null;
		String sql = "select x"
				+ " from ArquivoImagem x"
				+ " where x.pessoa = :pessoa";
		try {
			imagem = getEntityManager()
				.createQuery(sql, ArquivoImagem.class)
				.setParameter("pessoa", motorista.getPessoa())
				.getSingleResult();
		} catch (NoResultException e) {
			// Não reporta exceção caso não exista imagem.
		}
		return imagem;
	}

	public ArquivoImagem recuperarImagemVeiculo(Veiculo veiculo) throws Exception {
		ArquivoImagem imagem = null;
		String sql = "select x"
				+ " from ArquivoImagem x"
				+ " where x.veiculo = :veiculo";
		try {
			imagem = getEntityManager()
				.createQuery(sql, ArquivoImagem.class)
				.setParameter("veiculo", veiculo)
				.getSingleResult();
		} catch (NoResultException e) {
			// Não reporta exceção caso não exista imagem.
		}
		return imagem;
	}

	public ArquivoImagem recuperarImagemRota(Rota rota) throws Exception {
		ArquivoImagem imagem = null;
		String sql = "select x"
				+ " from ArquivoImagem x"
				+ " where x.rota = :rota";
		try {
			imagem = getEntityManager()
				.createQuery(sql, ArquivoImagem.class)
				.setParameter("rota", rota)
				.getSingleResult();
		} catch (NoResultException e) {
			// Não reporta exceção caso não exista imagem.
		}
		return imagem;
	}


}
