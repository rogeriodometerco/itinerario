package motor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import modelo.PontoRota;
import modelo.PosicaoVeiculo;
 
public class Viagem {
	private List<PosicaoVeiculo> posicoes;
	
	public Viagem(List<PosicaoVeiculo> posicoes) {
		this.posicoes = posicoes;
	}
	
	public List<PosicaoVeiculo> getPosicoes() {
		return posicoes;
	}
	
	public boolean parouEm(PontoRota ponto) {
		boolean parou = false;
		double distancia = 0;
		for (PosicaoVeiculo p: posicoes) {
			if (p.getVelocidade() == 0) { // veículo parado
				distancia = LatLngUtil.calcularDistancia(
						p.getLat(), p.getLng(), ponto.getLat(), ponto.getLng()); 
				if (distancia <= LatLngUtil.DISTANCIA_LIMITE_DO_LOCAL_DE_PARADA) {
					parou = true;
					break;
				}
			}
		}
		return parou;
	}
		
	/**
	 * Calcula distância percorrida em km.
	 * @return
	 */
	public double getDistanciaPercorrida() {
		double distancia = 0;
		PosicaoVeiculo p1 = null;
		PosicaoVeiculo p2 = null;
		for (PosicaoVeiculo p: posicoes) {
			if (p1 == null) {
				p1 = p;
			} else {
				if (p2 == null) {
					p2 = p;
				} else {
					p1 = p2;
					p2 = p;
				}
				distancia += MathUtil.calcularDistanciaEmKm(p1, p2);
			}
		}
		return distancia;
	}
	
	public Date getDataReferencia() {
		//TODO verificar se <posicoes> sempre terá elementos.
		Calendar c = Calendar.getInstance();
		c.setTime(this.posicoes.get(0).getDataHora());
		c.set(Calendar.HOUR_OF_DAY, 0);
		return c.getTime();
	}
	
}
