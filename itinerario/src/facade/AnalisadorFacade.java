package facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.ProgramacaoRota;

@Stateless 
public class AnalisadorFacade {
	@EJB
	private ProgramacaoRotaFacade programacaoFacade;

	/**
	 * Gera as análises de movimentação de veículo numa data conforme 
	 * a programação de suas rotas. 
	 * @param data
	 */
	public void gerarAnalises(Date data) throws Exception {
		List<ProgramacaoRota> programacoes = programacaoFacade.recuperarProgramacoes(data);
		for (ProgramacaoRota p: programacoes) {
			
		}
	}
}
