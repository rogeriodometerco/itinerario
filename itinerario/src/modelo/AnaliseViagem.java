package modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AnaliseViagem {
	@Id
	@GeneratedValue
	private Long id;
	private Date dataViagem;
	private Date dataAnalise;
	@ManyToOne
	private ProgramacaoRota programacao;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="analiseViagem", orphanRemoval=true)
	private List<AnalisePosicao> analisesPosicao;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="analiseViagem", orphanRemoval=true)
	private List<AnaliseParada> analisesParada;
	@ManyToOne
	private FechamentoRota fechamentoRota;
	@ManyToOne
	private Veiculo veiculo;
	@ManyToOne
	private Rota rota;
	private Double kmPrevisto;
	private Double kmRealizado;
	private Double kmNoTrajeto;
	private Double kmForaTrajeto;
	private Integer paradasPrevistas;
	private Integer paradasCumpridas;
	private Double kmPago;
	private Double valorKm;
	private Double valorPago;
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
	public List<AnalisePosicao> getAnalisesPosicao() {
		return analisesPosicao;
	}
	public void setAnalisesPosicao(List<AnalisePosicao> analisesPosicao) {
		this.analisesPosicao = analisesPosicao;
	}
	public List<AnaliseParada> getAnalisesParada() {
		return analisesParada;
	}
	public void setAnalisesParada(List<AnaliseParada> analisesParada) {
		this.analisesParada = analisesParada;
	}
	public FechamentoRota getFechamentoRota() {
		return fechamentoRota;
	}
	public void setFechamentoRota(FechamentoRota fechamentoRota) {
		this.fechamentoRota = fechamentoRota;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public Rota getRota() {
		return rota;
	}
	public void setRota(Rota rota) {
		this.rota = rota;
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
	public Double getValorPago() {
		return valorPago;
	}
	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
	
