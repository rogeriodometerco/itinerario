package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PontoLinha {
	@Id
	@GeneratedValue
	private Long id;
	private Integer sequencia;
	private Double lat;
	private Double lng;
	@ManyToOne
	private Linha linha;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getSequencia() {
		return sequencia;
	}
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Linha getLinha() {
		return linha;
	}
	public void setLinha(Linha linha) {
		this.linha = linha;
	}
}
