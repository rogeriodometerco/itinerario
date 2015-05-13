package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Atendido;
import modelo.Passageiro;
import modelo.ProgramacaoRota;
import modelo.Rota;
import util.Paginador;
import dao.AtendidoDao;

@Stateless
public class AtendidoFacade
extends GenericCrudFacade<Atendido> {

	@EJB
	private AtendidoDao dao;

	@Override
	protected AtendidoDao getDao() {
		return dao;
	}

	public List<Atendido> listar(Rota rota) 
			throws Exception {
		String sql = "select a"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.getResultList();
	}

	public List<Atendido> listar(Rota rota, Paginador paginador) 
			throws Exception {
		String sql = "select a"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Atendido> listar(Passageiro passageiro) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("passageiro", passageiro)
				.getResultList();
	}

	public List<Atendido> listar(Passageiro passageiro, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("passageiro", passageiro)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Atendido> listar(Rota rota, Passageiro passageiro) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota"
				+ " and a.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.setParameter("passageiro", passageiro)
				.getResultList();
	}

	public List<Atendido> listar(Rota rota, Passageiro passageiro, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as a, ProgramacaoRota as p" 
				+ " where a.programacaoRota = p"
				+ " and p.rota = :rota"
				+ " and a.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("rota", rota)
				.setParameter("passageiro", passageiro)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota, Passageiro passageiro) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota"
				+ " and x.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.setParameter("passageiro", passageiro)
				.getResultList();
	}

	public List<Atendido> listar(ProgramacaoRota programacaoRota, Passageiro passageiro, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Atendido as x" 
				+ " where x.programacaoRota = :programacaoRota"
				+ " and x.passageiro = :passageiro";
		return getEntityManager()
				.createQuery(sql, Atendido.class)
				.setParameter("programacaoRota", programacaoRota)
				.setParameter("passageiro", passageiro)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	@Override
	protected void validar(Atendido entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getProgramacaoRota() == null) {
			erros.add("Informe a programação da rota");
		}
		if (entidade.getPassageiro() == null) {
			erros.add("Informe o passageiro");
		}
		if (entidade.getPontoParada() == null) {
			erros.add("Informe o ponto de parada");
		}
		if (entidade.getId() != null) {
			Atendido atendido = recuperar(entidade.getId());
			if (atendido.getId().equals(entidade.getId())) {
				if (!atendido.getPassageiro().getId().equals(entidade.getPassageiro().getId())) {
					erros.add("Passageiro não pode ser alterado, exclua o registro ou cadastre um novo");
				}
			}
		}
		if (entidade.getId() == null && entidade.getProgramacaoRota() != null 
				&& entidade.getPassageiro() != null) {
			if (listar(entidade.getProgramacaoRota(), entidade.getPassageiro()).size() > 0) {
				erros.add("Passageiro já está vinculado a esta programação de viagem");
			}
			
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}