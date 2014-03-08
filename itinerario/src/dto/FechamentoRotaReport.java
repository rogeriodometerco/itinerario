package dto;

import java.util.ArrayList;
import java.util.List;

import modelo.Escola;
import modelo.FechamentoRota;
import modelo.Pessoa;

public class FechamentoRotaReport {
	private FechamentoRota fechamentoRota;
	private List<Pessoa> pessoas;
	private List<Escola> escolas;
	private List<NomeAssinatura> nomesAssinatura;
	private List<List<NomeAssinatura>> nomesAssinaturaLista;
	private String escolasToString;
	
	public FechamentoRotaReport(FechamentoRota fechamentoRota, List<Pessoa> pessoas, 
			List<Escola> escolas) {
		this.fechamentoRota = fechamentoRota;
		this.pessoas = pessoas;
		this.escolas = escolas;
		definirEscolasToString();
		definirNomesAssinatura();
		definirNomesAssinaturaLista();
	}

	private void definirNomesAssinatura() {
		nomesAssinatura =  new ArrayList<NomeAssinatura>();
		nomesAssinatura.add(new NomeAssinatura("Dino da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("Jefferson da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("José da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("Pedro da Silva"));
	}

	private void definirNomesAssinaturaLista() {
		nomesAssinaturaLista = new ArrayList<List<NomeAssinatura>>();
		List<NomeAssinatura> nomesAssinatura = new ArrayList<NomeAssinatura>();
		nomesAssinatura.add(new NomeAssinatura("Dino da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("Jefferson da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("José da Silva Sauro"));
		nomesAssinatura.add(new NomeAssinatura("Pedro da Silva"));
		nomesAssinaturaLista.add(nomesAssinatura);
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
	
}
