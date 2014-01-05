package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import modelo.PontoRota;

@Entity
public class AnaliseParada {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private AnaliseViagem analiseViagem;
	@ManyToOne
	private PontoRota pontoParada;
	private Boolean cumprida;
	
	public AnaliseParada() {
		
	}
	
	public AnaliseParada(AnaliseViagem analiseViagem, PontoRota pontoParada, boolean cumprida) {
		this.analiseViagem = analiseViagem;
		this.pontoParada = pontoParada;
		this.cumprida = cumprida;
	}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AnaliseViagem getAnaliseViagem() {
		return analiseViagem;
	}
	public void setAnaliseViagem(AnaliseViagem analiseViagem) {
		this.analiseViagem = analiseViagem;
	}
	public PontoRota getPontoParada() {
		return pontoParada;
	}
	public void setPontoParada(PontoRota pontoParada) {
		this.pontoParada = pontoParada;
	}
	public Boolean getCumprida() {
		return cumprida;
	}
	public void setCumprida(Boolean cumprida) {
		this.cumprida = cumprida;
	}
	
	
}
