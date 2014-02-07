package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.Calendario;
import dao.CalendarioDao;

@Stateless
public class CalendarioFacade
extends GenericCrudFacade<Calendario> {

	@EJB
	private CalendarioDao dao;

	@Override
	protected CalendarioDao getDao() {
		return dao;
	}

	public List<Calendario> listarPorNomeContendo(String parteDoNome) 
			throws Exception {
		String sql = "select x"
				+ " from Calendario x "
				+ " where upper(x.nome) like :chave";
		return getEntityManager().createQuery(sql, Calendario.class)
				.setParameter("chave", "%" + parteDoNome.toUpperCase() + "%")
				.getResultList();
	}

	public boolean isDiaUtil(Calendario calendario, Date data) {
		// TODO
		return true;
	}
}