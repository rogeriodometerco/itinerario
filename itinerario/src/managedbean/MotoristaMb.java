package managedbean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import modelo.ArquivoImagem;
import modelo.Motorista;
import modelo.Pessoa;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import util.ArquivoUtil;
import util.JsfUtil;
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
	@EJB
	private MotoristaFacade facade;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
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
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para motorista: " + e.getMessage());
		}
		return null;
	}

	public void listar() { 
		try {
			if (chavePesquisa == null) {
				this.lista = facade.listar();
			} else {
				this.lista = facade.autocomplete(chavePesquisa);
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
	}

	public void iniciarAlteracao(Motorista motorista) {
		try {
			this.motorista = facade.recuperarParaEdicao(motorista.getId());
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar motorista para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			facade.salvar(motorista);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}

	}

	public void iniciarExclusao(Motorista motorista) {
		try {
			this.motorista = facade.recuperarParaExclusao(motorista.getId());
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar motorista para exclusão: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(motorista);
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

	public String getChavePesquisa() {
		return chavePesquisa;
	}

	public void setChavePesquisa(String chave) { 
		this.chavePesquisa = chave;
	}

	public void arquivoCarregado(FileUploadEvent event) {
		try {
			ArquivoImagem arquivoImagem = ArquivoUtil.gravarArquivoImagem(event.getFile().getContents(),
					event.getFile().getFileName());
			arquivoImagem.setPessoa(motorista.getPessoa());
			if (motorista.getPessoa().getImagens() == null) {
				motorista.getPessoa().setImagens(new ArrayList<ArquivoImagem>());
			}
			motorista.getPessoa().getImagens().set(0, arquivoImagem);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao carregar arquivo: " + e.getMessage());
		}
	}

}