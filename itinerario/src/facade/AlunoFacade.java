package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Aluno;
import modelo.Escola;
import modelo.Passageiro;
import util.Paginador;
import dao.AlunoDao;

@Stateless
public class AlunoFacade extends GenericCrudFacade<Aluno> {

	@EJB
	private AlunoDao alunoDao;

	@Override
	protected AlunoDao getDao() {
		return alunoDao;
	}

	public List<Aluno> listar(Escola escola) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.escola = :escola";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("escola", escola)
				.getResultList();
	}

	public List<Aluno> listar(Escola escola, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.escola = :escola";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("escola", escola)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Aluno> listar(Passageiro passageiro) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.passageiro = :passageiro";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("passageiro", passageiro)
				.getResultList();
	}

	public List<Aluno> listar(Passageiro passageiro, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.passageiro = :passageiro";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("passageiro", passageiro)
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}

	public List<Aluno> listar(Escola escola, Passageiro passageiro) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.escola = :escola"
				+ " and x.passageiro = :passageiro";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("escola", escola)
				.setParameter("passageiro", passageiro)
				.getResultList();
	}

	@Override
	protected void validar(Aluno entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getPassageiro() == null) {
			erros.add("Informe o passageiro");
		}
		if (entidade.getEscola() == null) {
			erros.add("Informe a escola");
		}
		if (listar(entidade.getEscola(), entidade.getPassageiro()).size() > 0) {
			erros.add("Este passageiro já está cadastrado como aluno desta escola");
		}
		if (entidade.getId() != null) {
			Aluno aluno = recuperar(entidade.getId());
			if (aluno.getId().equals(entidade.getId())) {
				if (entidade.getPassageiro() != null) {
					if (!aluno.getPassageiro().getId().equals(entidade.getPassageiro().getId())) {
						erros.add("Passageiro não pode ser alterado, exclua o registro ou cadastre um novo");
					}
				}
				if (entidade.getEscola() != null) {
					if (!aluno.getEscola().getId().equals(entidade.getEscola().getId())) {
						erros.add("Escola não pode ser alterada, exclua o registro ou cadastre um novo");
					}
				}
			}
		}

		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

}
