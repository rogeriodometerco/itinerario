package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Atendido {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Pessoa pessoa;
	@ManyToOne
	private ProgramacaoRota programacaoRota;
	@ManyToOne
	private PontoRota pontoParada;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public ProgramacaoRota getProgramacaoRota() {
		return programacaoRota;
	}
	public void setProgramacaoRota(ProgramacaoRota programacaoRota) {
		this.programacaoRota = programacaoRota;
	}
	public PontoRota getPontoParada() {
		return pontoParada;
	}
	public void setPontoParada(PontoRota ponto) {
		this.pontoParada = ponto;
	}
}