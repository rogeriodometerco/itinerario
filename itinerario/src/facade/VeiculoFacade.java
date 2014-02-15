package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import modelo.Veiculo;
import dao.VeiculoDao;

@Stateless
public class VeiculoFacade
extends GenericCrudFacade<Veiculo> {

	@EJB
	private VeiculoDao dao;

	@Override
	protected VeiculoDao getDao() {
		return dao;
	}

	public Veiculo recuperarPorPlaca(String placa) 
			throws Exception {
		String sql = "select v from Veiculo as v"
				+ " where upper(v.placa) = :placa";
		try {
			return (Veiculo)getEntityManager().createQuery(sql)
					.setParameter("placa", placa.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	public List<Veiculo> listarPorParteDaPlaca(String parteDaPlaca) 
			throws Exception {

		TypedQuery<Veiculo> query = getEntityManager().createQuery(
				"select x from Veiculo x where upper(x.placa) like :placa", Veiculo.class);
		query.setParameter("placa", "%" + parteDaPlaca.toUpperCase() + "%");
		return query.getResultList();
	}

	@Override
	protected void validar(Veiculo entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getPlaca() == null || entidade.getPlaca().trim().equals("")) {
			erros.add("Informe a placa");
		}
		Veiculo v = recuperarPorPlaca(entidade.getPlaca());
		if (v != null && v.getPlaca().toUpperCase().equals(entidade.getPlaca().toUpperCase()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(v.getId()))) {
				erros.add("Já existe um veículo com a placa " + entidade.getPlaca());
			}
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void completarEdicao(Veiculo entidade) throws Exception {
		if (entidade.getPlaca() != null) {
			entidade.setPlaca(entidade.getPlaca().toUpperCase());
		}
	}
}
