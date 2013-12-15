package facade;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.ProgramacaoLinha;
import dao.ProgramacaoLinhaDao;


@Stateless
public class ProgramacaoLinhaFacade 
	extends GenericCrudFacade<ProgramacaoLinha> {

	@EJB
	private ProgramacaoLinhaDao dao;
	
	@Override
	protected ProgramacaoLinhaDao getDao() {
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgramacaoLinha> recuperarProgramacoes(Date dataInicial, Date dataFinal) throws Exception {
		// TODO filtro considerando a data.

		Calendar c1 = Calendar.getInstance();
		c1.setTime(dataInicial);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(dataFinal);
		Set<Integer> diasSemana = new HashSet<Integer>();
		while (c1.compareTo(c2) <= 0) {
			diasSemana.add(c1.get(Calendar.DAY_OF_WEEK));
			c1.add(Calendar.DAY_OF_MONTH, 1);
		}

		// Período contém todos os dias da semana.
		if (diasSemana.size() == 7) {
			System.out.println("ProgramacaoLinha - listar()");
			return listar();
		} else {
			String sql = "select p from ProgramacaoLinha as p"
					 + " where p.diaSemana in (:diasSemana)";
			System.out.println("ProgramacaoLinha - filtro");
			return (List<ProgramacaoLinha>)getEntityManager().createQuery(sql)
				.setParameter("diasSemana", diasSemana)
				.getResultList();
		}
	}

}