package motor;

public class LatLngUtil {

	public static final int EQUIVALENCIA_GRAU_EM_METRO = 111133;
	public static final int DISTANCIA_LIMITE_DENTRO_DO_TRAJETO = 100;
	
	/**
	 * Calcula a distância entre dois pontos.
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return distância em metros.
	 */
	public static double calcularDistancia(double lat1, double lng1, double lat2, double lng2) {
		Double cateto1 = Math.abs(lat2) - Math.abs(lat1);
		Double cateto2 = Math.abs(lng2) - Math.abs(lng1);
		return Math.sqrt(Math.pow(cateto1, 2) + Math.pow(cateto2, 2)) * EQUIVALENCIA_GRAU_EM_METRO;
	}
	
}
