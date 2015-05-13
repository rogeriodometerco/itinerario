package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.ArquivoImagem;
import modelo.Veiculo;

import org.primefaces.event.FileUploadEvent;

import util.ArquivoUtil;
import util.JsfUtil;
import util.Paginador;
import facade.ArquivoImagemFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class VeiculoMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Veiculo veiculo;
	private List<Veiculo> lista;
	private String placaPesquisa;
	private String estadoView;
	private String caminhoImagem;
	private ArquivoImagem arquivoImagem;
	@EJB
	private VeiculoFacade facade;
	@EJB
	private ArquivoImagemFacade arquivoImagemFacade;
	
	private Paginador paginador;
	
	//@ManagedProperty(value="#{rotaMb}")
	//private RotaMb rotaMb;

	@PostConstruct
	private void inicializar() {
		this.estadoView = LISTAGEM;
		this.paginador = new Paginador(10); 
	}
	
/*	public RotaMb getRotaMb() {
		return rotaMb;
	}
	
	public void setRotaMb(RotaMb r) {
		this.rotaMb = r;
	}

	public String teste() {
		JsfUtil.addMsgSucesso("A rota selecionada é: " + rotaMb.getRota().getCodigo());
		JsfUtil.addMsgSucesso("Quantidade de rotas listadas: " + rotaMb.getLista().size());
		rotaMb.getLista().get(0).setNome("rogerio fernando dometerco");
		//return "veiculo";
		return null;
	}
	*/
	public Veiculo getveiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public void listar() { 
		try {
			if (placaPesquisa == null || placaPesquisa.trim().length() == 0) {
				this.lista = facade.listar(paginador);
			} else {
				this.lista = facade.listarPorParteDaPlaca(placaPesquisa, paginador);
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar: " + e.getMessage());
		}
	}

	public List<Veiculo> autocomplete(String parteDaPlaca) {
		try {
			return facade.listarPorParteDaPlaca(parteDaPlaca);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar lista de sugestões para veículo: " + e.getMessage());
		}
		return null;
	}
	
	public List<Veiculo> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.veiculo = new Veiculo();
		this.veiculo.setImagens(new ArrayList<ArquivoImagem>());
		this.caminhoImagem = null;
		this.arquivoImagem = new ArquivoImagem();
	}

	public void iniciarAlteracao(Veiculo veiculo) {
		try {
			this.veiculo = facade.recuperarParaEdicao(veiculo.getId());
			//this.arquivoImagem = facade.recuperarArquivoImagem(veiculo.getId());
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemVeiculo(veiculo);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			this.estadoView = ALTERACAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar veículo para edição: " + e.getMessage());
		}
	}

	public void terminarCriacaoOuAlteracao() {
		try {
			if (arquivoImagem.getConteudo() == null) {
				veiculo.getImagens().clear();
			} else {
				if (veiculo.getImagens().size() == 0) {
					arquivoImagem.setVeiculo(veiculo);
					veiculo.getImagens().add(0, arquivoImagem);
				} else {
					veiculo.getImagens().get(0).setConteudo(arquivoImagem.getConteudo());
				}
			}
			facade.salvar(veiculo);
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			listar();
			this.estadoView = LISTAGEM;
			// TODO Remover arquivo de imagem.
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao salvar: " + e.getMessage());
		}
	}

	public void iniciarExclusao(Veiculo veiculo) {
		try {
			this.veiculo = facade.recuperarParaExclusao(veiculo.getId());
			//this.arquivoImagem = facade.recuperarArquivoImagem(veiculo.getId());
			this.arquivoImagem = arquivoImagemFacade.recuperarImagemVeiculo(veiculo);
			if (arquivoImagem == null) {
				arquivoImagem = new ArquivoImagem();
			}
			if (arquivoImagem.getConteudo() != null) {
				this.caminhoImagem = ArquivoUtil.criarArquivoParaExibicao(arquivoImagem);
			}
			this.estadoView = EXCLUSAO;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar veículo para exclusão: " + e.getMessage());
		}
	}

	public void terminarExclusao() {
		try {
			facade.excluir(veiculo);
			JsfUtil.addMsgSucesso("Informações excluídas com sucesso.");
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

	public String getPlacaPesquisa() {
		return placaPesquisa;
	}

	public void setPlacaPesquisa(String placaPesquisa) {
		this.placaPesquisa = placaPesquisa;
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