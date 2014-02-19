package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PontoRota {
	@Id
	@GeneratedValue
	private Long id;
	private Integer sequencia;
	private Double lat;
	private Double lng;
	private Boolean parada;
	private Integer numeroParada;
	private String descricao;
	@ManyToOne
	private Rota rota;

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
	public Boolean getParada() {
		return parada;
	}
	public void setParada(Boolean parada) {
		this.parada = parada;
	}
	public Integer getNumeroParada() {
		return numeroParada;
	}
	public void setNumeroParada(Integer numeroParada) {
		this.numeroParada = numeroParada;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Rota getRota() {
		return rota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lng == null) ? 0 : lng.hashCode());
		result = prime * result
				+ ((numeroParada == null) ? 0 : numeroParada.hashCode());
		result = prime * result + ((parada == null) ? 0 : parada.hashCode());
		result = prime * result + ((rota == null) ? 0 : rota.hashCode());
		result = prime * result
				+ ((sequencia == null) ? 0 : sequencia.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PontoRota other = (PontoRota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) { 
			return false;
		} else if (id.equals(other.id)) { // acrescentada esta condição na mão
			return true;
		}
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		if (numeroParada == null) {
			if (other.numeroParada != null)
				return false;
		} else if (!numeroParada.equals(other.numeroParada))
			return false;
		if (parada == null) {
			if (other.parada != null)
				return false;
		} else if (!parada.equals(other.parada))
			return false;
		if (rota == null) {
			if (other.rota != null)
				return false;
		} else if (!rota.equals(other.rota))
			return false;
		if (sequencia == null) {
			if (other.sequencia != null)
				return false;
		} else if (!sequencia.equals(other.sequencia))
			return false;
		return true;
	}
}