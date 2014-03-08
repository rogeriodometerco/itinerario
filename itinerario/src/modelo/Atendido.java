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
	private Passageiro passageiro;
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
	public Passageiro getPassageiro() {
		return passageiro;
	}
	public void setPassageiro(Passageiro passageiro) {
		this.passageiro = passageiro;
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