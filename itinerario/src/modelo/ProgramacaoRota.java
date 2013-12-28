package modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProgramacaoRota {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Rota rota;
	@Temporal(value=TemporalType.TIME)
	private Date horaInicial;
	@Temporal(value=TemporalType.TIME)
	private Date horaFinal;
	private Date inicioPeriodo;
	private Date terminoPeriodo;
	//@Enumerated(EnumType.ORDINAL)
	private CalendarioEnum tipoCalendario;
	@ManyToOne
	private CalendarioLetivo calendarioLetivo;
	@ManyToOne
	private Veiculo veiculo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Rota getRota() {
		return rota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
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
	public Date getInicioPeriodo() {
		return inicioPeriodo;
	}
	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}
	public Date getTerminoPeriodo() {
		return terminoPeriodo;
	}
	public void setTerminoPeriodo(Date terminoPeriodo) {
		this.terminoPeriodo = terminoPeriodo;
	}
	public CalendarioEnum getTipoCalendario() {
		return tipoCalendario;
	}
	public void setTipoCalendario(CalendarioEnum tipoCalendario) {
		this.tipoCalendario = tipoCalendario;
	}
	public CalendarioLetivo getCalendarioLetivo() {
		return calendarioLetivo;
	}
	public void setCalendarioLetivo(CalendarioLetivo calendarioLetivo) {
		this.calendarioLetivo = calendarioLetivo;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

}
