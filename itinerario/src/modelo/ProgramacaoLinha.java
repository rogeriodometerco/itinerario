package modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProgramacaoLinha {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Linha linha;
	private Integer diaSemana;
	@Temporal(value=TemporalType.TIME)
	private Date horaInicial;
	@Temporal(value=TemporalType.TIME)
	private Date horaFinal;
	@ManyToOne
	private Veiculo veiculo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Linha getLinha() {
		return linha;
	}
	public void setLinha(Linha linha) {
		this.linha = linha;
	}
	public Integer getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}
	public Date getHoraInicial() {
		return horaInicial;
	}
	public void setHoraInicial(Date horaInicial) {
		this.horaInicial = horaInicial;
	}
	public Date getHoraFinal() {
		return horaFinal;
	}
	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

}
