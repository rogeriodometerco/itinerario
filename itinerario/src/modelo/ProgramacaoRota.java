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
	private Date inicioVigencia;
	private Date terminoVigencia;
	@ManyToOne
	private Calendario calendario;
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
	public Calendario getCalendario() {
		return calendario;
	}
	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

}