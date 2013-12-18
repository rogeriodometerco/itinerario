package facade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.PosicaoVeiculo;
import modelo.ProgramacaoRota;
import modelo.Veiculo;
import dao.ProgramacaoRotaDao;


@Stateless
public class ProgramacaoRotaFacade 
	extends GenericCrudFacade<ProgramacaoRota> {

	@EJB
	private ProgramacaoRotaDao dao;
	
	@Override
	protected ProgramacaoRotaDao getDao() {
		return dao;
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date dataInicial, Date dataFinal) throws Exception {
		// TODO filtro .
		return dao.listar();

		/*
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
			return listar();
		} else {
			String sql = "select p from ProgramacaoRota as p"
					 + " where p.diaSemana in (:diasSemana)";
			return (List<ProgramacaoRota>)getEntityManager().createQuery(sql)
				.setParameter("diasSemana", diasSemana)
				.getResultList();
		}
		*/
	}

	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		// TODO Implementar.
		return dao.listar();

	}
	

}