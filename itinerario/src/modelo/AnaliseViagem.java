package modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AnaliseViagem {
	@Id
	@GeneratedValue
	private Long id;
	private Date dataViagem;
	private Date dataAnalise;
	@ManyToOne
	private ProgramacaoRota programacao;
	private Double kmPrevisto;
	private Double kmRealizado;
	private Double kmNoTrajeto;
	private Double kmForaTrajeto;
	private Double kmCumprida;
	private Integer paradasPrevistas;
	private Integer paradasCumpridas;
	private Double kmPago;
	private Double valorKm;
	private String observacao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDataViagem() {
		return dataViagem;
	}
	public void setDataViagem(Date dataViagem) {
		this.dataViagem = dataViagem;
	}
	public Date getDataAnalise() {
		return dataAnalise;
	}
	public void setDataAnalise(Date dataAnalise) {
		this.dataAnalise = dataAnalise;
	}
	public ProgramacaoRota getProgramacao() {
		return programacao;
	}
	public void setProgramacao(ProgramacaoRota programacao) {
		this.programacao = programacao;
	}
	public Double getKmPrevisto() {
		return kmPrevisto;
	}
	public void setKmPrevisto(Double kmPrevisto) {
		this.kmPrevisto = kmPrevisto;
	}
	public Double getKmRealizado() {
		return kmRealizado;
	}
	public void setKmRealizado(Double kmRealizado) {
		this.kmRealizado = kmRealizado;
	}
	public Double getKmNoTrajeto() {
		return kmNoTrajeto;
	}
	public void setKmNoTrajeto(Double kmNoTrajeto) {
		this.kmNoTrajeto = kmNoTrajeto;
	}
	public Double getKmForaTrajeto() {
		return kmForaTrajeto;
	}
	public void setKmForaTrajeto(Double kmForaTrajeto) {
		this.kmForaTrajeto = kmForaTrajeto;
	}
	public Double getKmCumprida() {
		return kmCumprida;
	}
	public void setKmCumprida(Double kmCumprida) {
		this.kmCumprida = kmCumprida;
	}
	public Integer getParadasPrevistas() {
		return paradasPrevistas;
	}
	public void setParadasPrevistas(Integer paradasPrevistas) {
		this.paradasPrevistas = paradasPrevistas;
	}
	public Integer getParadasCumpridas() {
		return paradasCumpridas;
	}
	public void setParadasCumpridas(Integer paradasCumpridas) {
		this.paradasCumpridas = paradasCumpridas;
	}
	public Double getKmPago() {
		return kmPago;
	}
	public void setKmPago(Double kmPago) {
		this.kmPago = kmPago;
	}
	public Double getValorKm() {
		return valorKm;
	}
	public void setValorKm(Double valorKm) {
		this.valorKm = valorKm;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
	
