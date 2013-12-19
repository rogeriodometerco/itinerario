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
	 * Gera as an�lises de movimenta��o de ve�culo numa data conforme 
	 * a programa��o de suas rotas. 
	 * @param data
	 */
	public void gerarAnalises(Date data) throws Exception {
		List<ProgramacaoRota> programacoes = programacaoFacade.recuperarProgramacoes(data);
		for (ProgramacaoRota p: programacoes) {
			
		}
	}
}
