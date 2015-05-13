package dto;

import java.util.ArrayList;
import java.util.List;

import util.JsfUtil;

import modelo.Escola;
import modelo.FechamentoRota;
import modelo.Pessoa;

public class FechamentoRotaReport {
	private FechamentoRota fechamentoRota;
	private byte[] imagemMotorista;
	private byte[] imagemRota;
	private String caminhoLogo;
	private List<Pessoa> pessoas;
	private List<Escola> escolas;
	private List<NomeAssinatura> nomesAssinatura;
	private String escolasToString;
	
	public FechamentoRotaReport(FechamentoRota fechamentoRota, List<Pessoa> pessoas, 
			List<Escola> escolas, byte[] imagemMotorista, byte[] imagemRota, 
			List<NomeAssinatura> nomesAssinatura) {
		this.fechamentoRota = fechamentoRota;
		this.pessoas = pessoas;
		this.escolas = escolas;
		this.imagemMotorista = imagemMotorista;
		this.imagemRota = imagemRota;
		this.nomesAssinatura = nomesAssinatura;
		this.caminhoLogo = JsfUtil.getExternalContext().getRealPath("/resources/imagens/logo.jpg");
		definirEscolasToString();
	}


	private void definirEscolasToString() {
		this.escolasToString = new String();
		for (Escola e: this.escolas) {
			if (escolasToString.length() > 0) {
				escolasToString += ", ";
			}
			escolasToString += e.getNome();
		}
	}
	
	public FechamentoRota getFechamentoRota() {
		return fechamentoRota;
	}
	public List<Escola> getEscolas() {
		return escolas;
	}
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	public String getEscolasToString() {
		return escolasToString;
	}

	public List<NomeAssinatura> getNomesAssinatura() {
		return nomesAssinatura;
	}

	public byte[] getImagemMotorista() {
		return imagemMotorista;
	}

	public byte[] getImagemRota() {
		return imagemRota;
	}

	public String getCaminhoLogo() {
		return caminhoLogo;
	}
	
}
