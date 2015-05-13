package facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import modelo.Usuario;
import util.JsfUtil;
import dao.UsuarioDao;

@Stateless
public class UsuarioFacade extends GenericCrudFacade<Usuario> {

	@EJB
	private UsuarioDao usuarioDao;
	
	@Override
	protected UsuarioDao getDao() {
		return usuarioDao;
	}

	public void alterarSenha(String atual, String nova, String confirmacao) 
		throws Exception {
		if (!nova.equals(confirmacao)) {
			throw new Exception("Confirma��o da senha n�o bate com a nova senha");
		}
		Usuario usuario = recuperarPorLogin(JsfUtil.getLogin());
		if (!atual.equals(usuario.getSenha())) {
			throw new Exception("Senha atual n�o bate com a senha do usu�rio");
		}
		usuario.setSenha(nova);
		salvar(usuario);
	}
	
	public Usuario recuperarPorLogin(String login) 
			throws Exception {
		String sql = "select x from Usuario as x"
				+ " where x.login = :login";
		try {
			return getEntityManager().createQuery(sql, Usuario.class)
					.setParameter("login", login)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} 
	}

	@Override
	protected void validar(Usuario entidade) throws Exception {
		List<String> erros = new ArrayList<String>();
		if (entidade.getLogin() == null || entidade.getLogin().trim().equals("") 
				|| entidade.getLogin().trim().length() < 5) {
			erros.add("Informe o login com no m�nimo 5 caracteres");
		}
		if (entidade.getSenha() == null || entidade.getSenha().trim().equals("") 
				|| entidade.getSenha().trim().length() < 5) {
			erros.add("Informe a senha com no m�nimo 5 caracteres");
		}
		if (entidade.getRole() == null || entidade.getRole().trim().equals("")) {
			erros.add("Informe o perfil do usu�rio");
		}
		if (entidade.getNome() == null || entidade.getNome().trim().equals("")) {
			erros.add("Informe o nome do usu�rio");
		}
		Usuario e = recuperarPorLogin(entidade.getLogin());
		if (e != null && e.getLogin().equals(entidade.getLogin()) ) {
			if ( (entidade.getId() == null || !entidade.getId().equals(e.getId()))) {
				erros.add("J� existe um usu�rio com este login");
			}
		}
		if (JsfUtil.getLogin().equals(entidade.getLogin())) {
			if (!entidade.getRole().equals(e.getRole())) {
				erros.add("Voc� n�o pode alterar o seu pr�prio perfil");
			}
			if (!entidade.getInativo().equals(e.getInativo())) {
				erros.add("Voc� n�o pode alterar o seu pr�prio indicador de inativo");
			}
		}
		if (erros.size() > 0) {
			throw new Exception(erros.toString());
		}
	}

	@Override
	protected void completarEdicao(Usuario entidade) throws Exception {
		if (entidade.getId() == null) {
			if (entidade.getInativo() == null) {
				entidade.setInativo(false);
			}
			if (entidade.getSenha() == null) {
				entidade.setSenha(entidade.getLogin());
			}
		}
	}
}
