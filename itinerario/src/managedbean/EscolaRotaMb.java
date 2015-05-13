package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Escola;
import modelo.EscolaRota;
import modelo.Rota;
import util.JsfUtil;
import util.Paginador;
import facade.EscolaRotaFacade;

@ManagedBean
@ViewScoped
public class EscolaRotaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private EscolaRota escolaRota;
	private List<EscolaRota> lista;
	private String estadoView;
	private Rota rotaPesquisa;
	private Escola escolaPesquisa;
	@EJB
	private EscolaRotaFacade facade;
	private Paginador paginador;
	
	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10);
	}

	public EscolaRota getEscolaRota() {
		return escolaRota;
	}

	public void setEscolaRota(EscolaRota escolaRota) {
		this.escolaRota = escolaRota;
	}

	public void listar() { 
		try {
			if (rotaPesquisa == null && escolaPesquisa == null) {
				this.lista = facade.listar(paginador);
			} else if (rotaPesquisa != null && escolaPesquisa == null) {
				this.lista = facade.listar(rotaPesquisa, paginador);
			} else if (rotaPesquisa == null && escolaPesquisa != null) {
				this.lista = facade.listar(escolaPesquisa, paginador);
			} else if (rotaPesquisa != null && escolaPesquisa != null) {
				this.lista = facade.listar(rotaPesquisa, escolaPesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<EscolaRota> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.escolaRota = new EscolaRota();
	}

	public void iniciarAlteracao(EscolaRota escolaRota) {
		this.escolaRota = escolaRota;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(escolaRota);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(EscolaRota escolaRota) {
		this.escolaRota = escolaRota;
		this.estadoView = EXCLUSAO;
	}

	public void terminarExclusao() {
		try {
			facade.excluir(escolaRota);
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

	public Rota getRotaPesquisa() {
		return rotaPesquisa;
	}

	public void setRotaPesquisa(Rota rota) { 
		this.rotaPesquisa = rota;
	}

	public Escola getEscolaPesquisa() {
		return escolaPesquisa;
	}

	public void setEscolaPesquisa(Escola veiculo) {
		this.escolaPesquisa = veiculo;
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