package modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Pessoa {
	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private String codigo;
	private String cpf;
	private String endereco;
	private Double lat;
	private Double lng;
	private Date dataNascimento;
	private String cnh;
	private Date validadeCnh;
	private String nomePai;
	private String nomeMae;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="pessoa", orphanRemoval=true)
	private List<ArquivoImagem> imagens;
	
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
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getCnh() {
		return cnh;
	}
	public void setCnh(String cnh) {
		this.cnh = cnh;
	}
	public Date getValidadeCnh() {
		return validadeCnh;
	}
	public void setValidadeCnh(Date validadeCnh) {
		this.validadeCnh = validadeCnh;
	}
	public String getNomePai() {
		return nomePai;
	}
	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}
	public String getNomeMae() {
		return nomeMae;
	}
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	public List<ArquivoImagem> getImagens() {
		return imagens;
	}
	public void setImagens(List<ArquivoImagem> imagens) {
		this.imagens = imagens;
	}
	
}
