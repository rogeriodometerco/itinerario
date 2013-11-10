package modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AgendamentoLinha {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Linha linha;
	private Integer diaSemanaInicial;
	private Integer diaSemanaFinal;
	@Temporal(value=TemporalType.TIME)
	private Date horaInicial;
	@Temporal(value=TemporalType.TIME)
	private Date horaFinal;
	private Date inicioVigencia;
	private Date terminoVigencia;
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
	public Integer getDiaSemanaInicial() {
		return diaSemanaInicial;
	}
	public void setDiaSemanaInicial(Integer diaSemanaInicial) {
		this.diaSemanaInicial = diaSemanaInicial;
	}
	public Integer getDiaSemanaFinal() {
		return diaSemanaFinal;
	}
	public void setDiaSemanaFinal(Integer diaSemanaFinal) {
		this.diaSemanaFinal = diaSemanaFinal;
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
	public Date getInicioVigencia() {
		return inicioVigencia;
	}
	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}
	public Date getTerminoVigencia() {
		return terminoVigencia;
	}
	public void setTerminoVigencia(Date terminoVigencia) {
		this.terminoVigencia = terminoVigencia;
	}
	
	
}
