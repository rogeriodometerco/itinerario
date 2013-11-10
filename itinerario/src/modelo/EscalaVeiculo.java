package modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EscalaVeiculo {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Veiculo veiculo;
	@ManyToOne
	private AgendamentoLinha agendamento;
	private Date inicioVigencia;
	private Date terminoVigencia;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public AgendamentoLinha getAgendamento() {
		return agendamento;
	}
	public void setAgendamento(AgendamentoLinha agendamento) {
		this.agendamento = agendamento;
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
