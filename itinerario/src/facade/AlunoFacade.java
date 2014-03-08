package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Aluno;
import modelo.Escola;
import modelo.MotoristaVeiculo;
import modelo.Pessoa;
import modelo.Rota;
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

	public List<Aluno> listar(Pessoa pessoa) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.pessoa = :pessoa";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("pessoa", pessoa)
				.getResultList();
	}

	public List<Aluno> listar(Escola escola, Pessoa pessoa) 
			throws Exception {
		String sql = "select x"
				+ " from Aluno as x" 
				+ " where x.escola = :escola"
				+ " and x.pessoa = :pessoa";

		return getEntityManager()
				.createQuery(sql, Aluno.class)
				.setParameter("escola", escola)
				.setParameter("pessoa", pessoa)
				.getResultList();
	}

	@Override
	protected void validar(Aluno entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getPessoa() == null) {
			erros.add("Informe a pessoa");
		}
		if (entidade.getEscola() == null) {
			erros.add("Informe a escola");
		}
		if (listar(entidade.getEscola(), entidade.getPessoa()).size() > 0) {
			erros.add("Esta pessoa já está cadastrada como aluno desta escola");
		}
		if (entidade.getId() != null) {
			Aluno aluno = recuperar(entidade.getId());
			if (aluno.getId().equals(entidade.getId())) {
				if (entidade.getPessoa() != null) {
					if (!aluno.getPessoa().getId().equals(entidade.getPessoa().getId())) {
						erros.add("Pessoa não pode ser alterada, exclua o registro ou cadastre um novo");
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
