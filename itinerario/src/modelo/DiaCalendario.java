package modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DiaCalendario {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Calendario calendario;
	@Temporal(TemporalType.DATE)
	private Date data;
	private Boolean util;
	private String observacao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Calendario getCalendario() {
		return calendario;
	}
	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Boolean getUtil() {
		return util;
	}
	public void setUtil(Boolean util) {
		this.util = util;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getDiaSemana() {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.getDisplayName(
				Calendar.DAY_OF_WEEK, Calendar.SHORT, new Locale("pt","br"));
	}
}
