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
	 * Recupera as programa��es de rota para uma data.
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ProgramacaoRota> recuperarProgramacoes(Date data) throws Exception {
		String sql = "select p from ProgramacaoRota as p"
				 + " where p.inicioPeriodo <= :data and p.terminoPeriodo >= :data";
		List<ProgramacaoRota> lista = (List<ProgramacaoRota>)getEntityManager()
				.createQuery(sql)
				.setParameter("data", data, TemporalType.TIMESTAMP)
				.getResultList();
		// Se a programa��o � baseada no calend�rio letivo, verifica se no calend�rio a data � dia �til.
		// Caso negativo, a programa��o � retirada da lista.
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