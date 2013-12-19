package modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CalendarioLetivo {
	@Id
	@GeneratedValue
	private Long id;
	private Integer ano;
	private String descricao;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="calendario", orphanRemoval=true)
	private List<DiaCalendario> dias;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<DiaCalendario> getDias() {
		return dias;
	}
	public void setDias(List<DiaCalendario> dias) {
		this.dias = dias;
	}
	
}
