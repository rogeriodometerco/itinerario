package facade;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import modelo.EscalaVeiculo;

@Stateless
public class AnalisadorFacade {

	@EJB
	private PosicaoVeiculoFacade posicaoVeiculoFacade;
	
	public void analisar(EscalaVeiculo escala, Date data) {
		
	}
}
