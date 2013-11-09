package modelo;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Linha {
	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private String descricao;
	private Double quilometragem;
	private Boolean ativa;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="linha")
	private List<PontoLinha> pontos;
	
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getQuilometragem() {
		return quilometragem;
	}
	public void setQuilometragem(Double quilometragem) {
		this.quilometragem = quilometragem;
	}
	public List<PontoLinha> getPontos() {
		return pontos;
	}
	public void setPontos(List<PontoLinha> pontos) {
		this.pontos = pontos;
	}
	public Boolean getAtiva() {
		return ativa;
	}
	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}

}
