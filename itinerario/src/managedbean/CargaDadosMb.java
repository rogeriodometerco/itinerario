package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.PosicaoVeiculo;
import modelo.Veiculo;
import motor.MensagemRMC;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import util.JsfUtil;
import facade.PosicaoVeiculoFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class CargaDadosMb implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EJB
	private VeiculoFacade veiculoFacade;
	@EJB
	private PosicaoVeiculoFacade posicaoFacade;

	private Veiculo veiculo;

	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public void arquivoCarregado(FileUploadEvent event) {
		if (veiculo == null) {
			JsfUtil.addMsgErro("Informe o veículo para carregar o arquivo");
			return;
		}
		UploadedFile arquivo = event.getFile(); 
		int numLinha = 0;
		int importadas = 0;
		try {
			List<PosicaoVeiculo> posicoes = new ArrayList<PosicaoVeiculo>();
			Scanner s = new Scanner(arquivo.getInputstream());
			PosicaoVeiculo posicao = null;
			MensagemRMC mensagem = null;
			String linhaArquivo = null;
			while (s.hasNext()) {
				numLinha++;
				linhaArquivo = s.next();
				mensagem = new MensagemRMC(linhaArquivo);
				posicao = new PosicaoVeiculo();
				posicao.setVeiculo(veiculo);
				posicao.setLat(mensagem.getLat());
				posicao.setLng(mensagem.getLng());
				posicao.setVelocidade(mensagem.getVelocidade());
				posicao.setDataHora(mensagem.getDataHora());
				if (posicaoFacade.recuperarPosicao(posicao.getVeiculo(), posicao.getDataHora()) == null) {
					posicoes.add(posicao);
					importadas++;
					
				}
			}
			posicaoFacade.salvar(posicoes);
			JsfUtil.addMsgSucesso(arquivo.getFileName() 
					+ " carregado com sucesso. " + numLinha + " linhas processadas. " + importadas + " linhas importadas.");

		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao importar arquivo. Linha " + numLinha + ". " + e.getMessage());
			e.printStackTrace();
		}
	}  
}