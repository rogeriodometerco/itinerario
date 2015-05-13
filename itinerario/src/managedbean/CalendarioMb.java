package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import modelo.Calendario;
import modelo.DiaCalendario;
import util.JsfUtil;
import util.Paginador;
import facade.CalendarioFacade;

@ManagedBean
@ViewScoped
public class CalendarioMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Calendario calendario;
	private Integer ano;
	private List<Calendario> lista;
	private String estadoView;
	private String nomePesquisa;

	@EJB
	private CalendarioFacade facade;
	private Paginador paginador;

	@PostConstruct
	private void inicializar() {
		this.ano = null;
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
	}

	public Calendario getcalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public void listar() { 
		try {
			if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {
				this.lista = facade.listar(paginador);
			} else {
				this.lista = facade.listarPorNomeContendo(nomePesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Calendario> autocomplete(String chave) {
		try {
			return facade.listarPorNomeContendo(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para calendario: " + e.getMessage());
		}
		return null;
	}

	public List<Calendario> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.calendario = new Calendario();
	}

	/*
	public void anoChange(ValueChangeEvent event) {
		calendario.setDias(facade.montarDias((Integer)event.getNewValue()));
		for (DiaCalendario dia: calendario.getDias()) {
			dia.setCalendario(calendario);
		}
	}
	*/
	
	public void gerarDias() {
		calendario.setDias(facade.montarDias(ano));
		for (DiaCalendario dia: calendario.getDias()) {
			dia.setCalendario(calendario);
		}
	}
	
	public void iniciarAlteracao(Calendario calendario) {
		try {
			this.calendario = facade.recuperarParaEdicao(calendario.getId());
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar calendário para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(calendario);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Calendario calendario) {
		try {
			this.calendario = facade.recuperarParaEdicao(calendario.getId());
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar calendário para exclusão: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(calendario);
			JsfUtil.addMsgSucesso("Informações excluídas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao excluir: " + e.getMessage());
		}
	}

	public void cancelar() {
		listar();
		this.estadoView = LISTAGEM;
	}

	public Boolean isListagem() {
		return this.estadoView != null && this.estadoView.equals(LISTAGEM);
	}

	public Boolean isCriacao() {
		return this.estadoView != null && this.estadoView.equals(CRIACAO);
	}

	public Boolean isAlteracao() {
		return this.estadoView != null && this.estadoView.equals(ALTERACAO);
	}

	public Boolean isExclusao() {
		return this.estadoView != null && this.estadoView.equals(EXCLUSAO);
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public Boolean temPaginaAnterior() {
		return paginador.getPaginaAtual() > 1;
	}

	public Boolean temProximaPagina() {
		if (lista == null) {
			return false;
		} else {
			return paginador.getTamanhoPagina() <= lista.size();
		}
	}

	public void paginaAnterior() {
		paginador.anterior();
		listar();
	}

	public void proximaPagina() {
		paginador.proxima();
		listar();
	}
}