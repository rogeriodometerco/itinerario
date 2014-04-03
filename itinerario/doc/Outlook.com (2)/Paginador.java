public class Paginador {
	private int paginaAtual;
	private int tamanhoPagina;

	public Paginador() {
		this.paginaAtual = 1;
		this.tamanhoPagina = 10;
	}

	public Paginador(int tamanhoPagina) {
		this.paginaAtual = 1;
		this.tamanhoPagina = tamanhoPagina;
	}

	public void anterior() {
		if (paginaAtual > 1) {
			paginaAtual--;
		}
	}

	public void proxima() {
		paginaAtual++;
	}

	public int primeiroRegistro() {
		return (paginaAtual * tamanhoPagina) + 1 - tamanhoPagina;
	}
}