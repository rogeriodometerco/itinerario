package motor;

import modelo.PosicaoVeiculo;

public class MathUtil {

	public static double calcularDistancia(PosicaoVeiculo p1, PosicaoVeiculo p2) {
		double segundos = Math.abs(p2.getDataHora().getTime() - p1.getDataHora().getTime()) / 1000;
		return p2.getVelocidade() * segundos / 3600;
	}
}
