package managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import modelo.Linha;
import modelo.PontoLinha;
import util.JsfUtil;
import facade.LinhaFacade;

@ManagedBean
@ViewScoped
public class LinhaMBean {

	@EJB
	private LinhaFacade linhaFacade;
	
	private static final String CRIAR_LINHA = "criarLinha";
	private static final String ATUALIZAR_LINHA = "atualizarLinha";
	private static final String EXCLUIR_LINHA = "excluirLinha"; 
	private static final String LISTAR_LINHAS = "listarLinhas";
	private static final String PERMANECER_NA_MESMA_PAGINA = null;
	private String modo = "";
	
	private Linha linha;
	private List<Linha> linhas;

	public Linha getLinha() {
		if (linha == null){
			linha = new Linha();
			linha.setPontos(new ArrayList<PontoLinha>());
		}
		return linha;
	}
	
	public Boolean emCriacao() {
		return modo.equals(CRIAR_LINHA);
	}

	public Boolean emEdicao() {
		return modo.equals(ATUALIZAR_LINHA);
	}
	
	public Boolean emListagem() {
		return modo.equals("");
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public List<Linha> getLinhas() {
		try {
			if (linhas == null) {
				linhas = linhaFacade.listar();
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao listar linhas: " + e.getMessage());
		}
		return linhas;
	}

	public String atualizarLinhaInicio(Linha linha){
		this.linha = linha;
		modo = ATUALIZAR_LINHA;
		//return ATUALIZAR_LINHA;
		return PERMANECER_NA_MESMA_PAGINA;
	}
	
	public String atualizarLinhaInicio() {
		return ATUALIZAR_LINHA;
	}
	
	public String atualizarLinhaFim() {
		try {
			linhaFacade.salvar(linha);
			linha = null;
			modo = "";
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao atualizar linha: " + e.getMessage());
			return PERMANECER_NA_MESMA_PAGINA;
		}
		JsfUtil.addMsgSucesso("Operação realizada com sucesso.");
		return LISTAR_LINHAS;
	}
	
	public String excluirLinhaInicio(Linha linha){
		System.out.println("Excluir linha " + linha.getNome());
		this.linha = linha;
		return EXCLUIR_LINHA;
	}
	
	public String excluirLinhaFim() {
		try {
			System.out.println("excluirLinhaFim() - " + linha.getId() + " - " + linha.getNome());
			linhaFacade.excluir(linha);
			linha = null;
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao excluir linha: " + e.getMessage());
			return PERMANECER_NA_MESMA_PAGINA;
		}
		JsfUtil.addMsgSucesso("Operação realizada com sucesso.");
		return LISTAR_LINHAS;
	}
	
	public String criarLinhaInicio() {
		modo = CRIAR_LINHA;
		return CRIAR_LINHA;
	}
	 
	public String criarLinhaFim() {
		try {
			linhaFacade.salvar(linha);
			linha = null;
			modo = "";
			
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao criar linha: " + e.getMessage());
			return PERMANECER_NA_MESMA_PAGINA;
		}
		JsfUtil.addMsgSucesso("Operação realizada com sucesso.");
		return LISTAR_LINHAS;
	}
	
	public String cancelar() {
		return LISTAR_LINHAS;
	}
}