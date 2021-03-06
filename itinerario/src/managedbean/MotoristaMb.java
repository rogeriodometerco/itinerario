package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.ArquivoImagem;
import modelo.Motorista;
import modelo.Pessoa;

import org.primefaces.event.FileUploadEvent;

import util.ArquivoUtil;
import util.JsfUtil;
import util.Paginador;
import facade.ArquivoImagemFacade;
import facade.MotoristaFacade;

@ManagedBean
@ViewScoped
public class MotoristaMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Motorista motorista;
	private List<Motorista> lista;
	private String estadoView;
	private String chavePesquisa;
	private String caminhoImagem;
	private ArquivoImagem arquivoImagem;

	@EJB
	private MotoristaFacade facade;
	@EJB
	private ArquivoImagemFacade arquivoImagemFacade;
	
	private Paginador paginador;


	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		paginador = new Paginador(10);
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public List<Motorista> autocomplete(String chave) {
		try {
			return facade.autocomplete(chave);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugest�es para motorista: " + e.getMessage());
		}
		return null;
	}

	public void listar() { 
		try {
			if (chavePesquisa == null) {
				this.lista = facade.listar(paginador);
			} else {
				this.lista = facade.autocomplete(chavePesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Motorista> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.motorista = new Motorista();
		this.motorista.setPessoa(new Pessoa());
		this.motorista.getPessoa().setImagens(new ArrayList<ArquivoImagem>());
		this.caminhoImagem = null;
		this.arquivoImagem = new ArquivoImagem();
	}

	public void iniciarAlteracao(Motorista motorista) {
		try {
			this.motorista = facade.recuperarParaEdicao(motorista.getId());
			//this.arquivoImagem = facade.recuperarArquivoImagem(motorista.getId());
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemMotorista(motorista);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar motorista para edi��o: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			if (arquivoImagem.getConteudo() == null) {
				motorista.getPessoa().getImagens().clear();
			} else {
				if (motorista.getPessoa().getImagens().size() == 0) {
					arquivoImagem.setPessoa(motorista.getPessoa());
					motorista.getPessoa().getImagens().add(0, arquivoImagem);
				} else {
					motorista.getPessoa().getImagens().get(0).setConteudo(arquivoImagem.getConteudo());
				}
			}
			facade.salvar(motorista);
			JsfUtil.addMsgSucesso("Informa��es salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
			// TODO Remover arquivo de imagem.
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}
	}

	public void iniciarExclusao(Motorista motorista) {
		try {
			this.motorista = facade.recuperarParaExclusao(motorista.getId());
			//this.arquivoImagem = facade.recuperarArquivoImagem(motorista.getId());
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemMotorista(motorista);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar motorista para exclus�o: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(motorista);
			JsfUtil.addMsgSucesso("Informa��es exclu�das com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
			// TODO Remover arquivo de imagem.
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

	public String getChavePesquisa() {
		return chavePesquisa;
	}

	public void setChavePesquisa(String chave) { 
		this.chavePesquisa = chave;
	}

	public String getCaminhoImagem() {
		return caminhoImagem;
	}

	public void limparImagem() {
		this.arquivoImagem.setConteudo(null);
		this.caminhoImagem = "";
	}

	public void arquivoCarregado(FileUploadEvent event) {
		arquivoImagem.setConteudo(event.getFile().getContents());
		try {
			this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
		}
	}

}