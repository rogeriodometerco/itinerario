package motor;

import modelo.PosicaoVeiculo;

public class MathUtil {

	public static double calcularDistanciaEmKm(PosicaoVeiculo p1, PosicaoVeiculo p2) {
		double segundos = Math.abs(p2.getDataHora().getTime() - p1.getDataHora().getTime()) / 1000;
		return p2.getVelocidade() * segundos / 3600;
	}

	public static double calcularDistanciaEmMetros(PosicaoVeiculo p1, PosicaoVeiculo p2) {
		double segundos = Math.abs(p2.getDataHora().getTime() - p1.getDataHora().getTime()) / 1000;
		return p2.getVelocidade() * 1000 * segundos / 3600;
	}
}
