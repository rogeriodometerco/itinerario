package facade;

import java.util.Date;
import java.util.List;

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
	
	public List<ProgramacaoLinha> recuperarProgramacoes(Date dataInicial, Date dataFinal) throws Exception {
		// TODO filtro considerando a data.
		return listar();
	}

}