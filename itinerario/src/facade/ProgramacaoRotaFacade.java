package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;

import modelo.CalendarioEnum;
import modelo.ProgramacaoRota;
import dao.ProgramacaoRotaDao;


@Stateless
public class ProgramacaoRotaFacade 
	extends GenericCrudFacade<ProgramacaoRota> {

	@EJB
	private ProgramacaoRotaDao dao;
	@EJB
	private CalendarioLetivoFacade calendarioFacade;
	
	@Override
	protected ProgramacaoRotaDao getDao() {
		return dao;
	}
		/**
	 * Recupera as programações de rota para uma data.
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		String sql = "select p from ProgramacaoRota as p"
				 + " where p.inicioperiodo <= :data and p.terminoPeriodo >= :data";
		List<ProgramacaoRota> lista = (List<ProgramacaoRota>)getEntityManager()
				.createQuery(sql)
				.setParameter("data", data, TemporalType.TIMESTAMP)
				.getResultList();
		// Se a programação é baseada no calendário letivo, verifica se no calendário a data é dia útil.
		// Caso negativo, a programação é retirada da lista.
		for (ProgramacaoRota prog: lista) {
			if (prog.getTipoCalendario().equals(CalendarioEnum.LETIVO) && prog.getCalendarioLetivo() != null) {
				if (!calendarioFacade.isDiaLetivo(prog.getCalendarioLetivo(), data)) {
					lista.remove(prog);
				}
			}
		}
		return lista;
	}
	

}