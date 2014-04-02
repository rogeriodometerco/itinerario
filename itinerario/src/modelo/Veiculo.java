package modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Veiculo {
	@Id
	@GeneratedValue
	private Long id;
	private String placa;
	private Integer anoFabricacao;
	private Integer ocupantes;
	private String cor;
	private String marca;
	private String modelo;
	private String chassi;
	private String proprietario;
	private String conservacao;
	@ManyToOne
	private Motorista motorista;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="veiculo", orphanRemoval=true)
	private List<ArquivoImagem> imagens;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public Integer getAnoFabricacao() {
		return anoFabricacao;
	}
	public void setAnoFabricacao(Integer anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}
	public Integer getOcupantes() {
		return ocupantes;
	}
	public void setOcupantes(Integer ocupantes) {
		this.ocupantes = ocupantes;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getChassi() {
		return chassi;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public String getProprietario() {
		return proprietario;
	}
	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}
	public String getConservacao() {
		return conservacao;
	}
	public void setConservacao(String conservacao) {
		this.conservacao = conservacao;
	}
	public Motorista getMotorista() {
		return motorista;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public List<ArquivoImagem> getImagens() {
		return imagens;
	}
	public void setImagens(List<ArquivoImagem> imagens) {
		this.imagens = imagens;
	}
}
