package util;

public class Paginador {
	private int paginaAtual;
	private int tamanhoPagina;

	
	public int getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public int getTamanhoPagina() {
		return tamanhoPagina;
	}

	public void setTamanhoPagina(int tamanhoPagina) {
		this.tamanhoPagina = tamanhoPagina;
	}

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
		return (paginaAtual * tamanhoPagina) - tamanhoPagina;
	}
}