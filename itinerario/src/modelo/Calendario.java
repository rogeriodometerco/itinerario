package modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Calendario {
	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	@OneToMany(mappedBy="calendario", cascade=CascadeType.ALL)
	@OrderBy(value="data")
	private List<DiaCalendario> dias;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<DiaCalendario> getDias() {
		return dias;
	}
	public void setDias(List<DiaCalendario> dias) {
		this.dias = dias;
	}

}