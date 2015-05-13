package facade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import util.Paginador;

import modelo.Calendario;
import modelo.DiaCalendario;
import dao.CalendarioDao;

@Stateless
public class CalendarioFacade
extends GenericCrudFacade<Calendario> {

	@EJB
	private CalendarioDao dao;
	@EJB
	private DiaCalendarioFacade diaCalendarioFacade;

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
	
	public List<Calendario> listarPorNomeContendo(String parteDoNome, Paginador paginador) 
			throws Exception {
		String sql = "select x"
				+ " from Calendario x "
				+ " where upper(x.nome) like :chave";
		return getEntityManager().createQuery(sql, Calendario.class)
				.setParameter("chave", "%" + parteDoNome.toUpperCase() + "%")
				.setFirstResult(paginador.primeiroRegistro())
				.setMaxResults(paginador.getTamanhoPagina())
				.getResultList();
	}
	
	public Calendario recuperarParaEdicao(Long id) throws Exception {
		String sql = "select x"
				+ " from Calendario x "
				+ " left join fetch x.dias " 
				+ " where x.id = :id";
		return getEntityManager().createQuery(sql, Calendario.class)
				.setParameter("id", id)
				.getSingleResult();
		/*
		Calendario calendario = recuperar(id);
		calendario.getDias().size();
		return calendario;
		*/
	}
	
	public List<DiaCalendario>montarDias(int ano) {
		List<DiaCalendario> dias = new ArrayList<DiaCalendario>();
		Calendar c = Calendar.getInstance();
		c.set(ano, 0, 1);
		DiaCalendario dia = null;
		boolean util = true;
		while (ano == c.get(Calendar.YEAR)) {
			if (c.get(Calendar.DAY_OF_YEAR) == 1) {
				util = false;
			} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
					|| c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				util = false;
			} else {
				util = true;
			}
			dia = new DiaCalendario();
			dia.setData(c.getTime());
			dia.setUtil(util);
			dias.add(dia);
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return dias;
	}

	public boolean isDiaUtil(Calendario calendario, Date data) {
		DiaCalendario dia = diaCalendarioFacade.recuperar(calendario, data);
		if (dia == null) {
			return false;
		} else {
			return dia.getUtil();
		}
	}
}