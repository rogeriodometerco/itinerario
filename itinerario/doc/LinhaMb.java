@ManagedBean
@ViewScoped
public class LinhaMb implements Serializable {
	private static final String LISTAGEM = "listagem";
	private static final String CRIACAO = "criacao";
	private static final String ALTERACAO = "alteracao";
	private static final String EXCLUSAO = "exclusao";
	private Linha linha;
	private List<Linha> lista;
	private String estadoDaView;
	@EJB
	private LinhaFacade facade;

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public void listar() {
		this.lista = facade.listar();
	}

	public List<Linha> getLista() {
		if (lista == null) {
			listar();
		}
		return this.lista;
	}

	public void iniciarCriacao() {
		this.estadoView = CRIACAO;
		this.linha = new Linha();
		this.linha.setPontos(new ArrayList<PontoLinha>());
	}

	public void iniciarAlteracao(Linha linha) {
		this.linha = linha;
		this.estadoView = ALTERACAO;
	}

	public void terminarCriacaoOuExclusao() {
		facade.salvar(linha);
		listar();
		this.estadoView = LISTAGEM;
	}

	public void iniciarExclusao(Linha linha) {
		this.linha = linha;
	}

	public void terminarExclusao() {
		facade.excluir(linha);
		listar();
		this.estadoView = LISTAGEM;
	}

	public void cancelar() {
		listar();
		this.estadoView = LISTAGEM;
	}

	public Boolean isListagem() {
		return this.estadoDaView.equals(LISTAGEM);
	}

	public Boolean isCriacao() {
		return this.estadoDaView.equals(CRIACAO);
	}

	public Boolean isAlteracao() {
		return this.estadoDaView.equals(ALTERACAO);
	}

	public Boolean isExclusao() {
		return this.estadoDaView.equals(EXCLUSAO);
	}

}