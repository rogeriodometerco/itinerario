package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;

import modelo.ArquivoImagem;

public class ArquivoUtil {

	public static String criarArquivoParaExibicao(ArquivoImagem arquivoImagem) throws Exception {
		String caminhoRelativo = "/imagemTemp";
		String caminhoCompleto = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath(caminhoRelativo);
		
		// Nome do arquivo.
		String arquivo = String.valueOf(System.currentTimeMillis());
		if (arquivoImagem.getId() != null) {
			arquivo = arquivoImagem.getId().toString();
		}
		arquivo = arquivo + ".jpeg";
		
		String nomeCompleto = caminhoCompleto + "/" + arquivo;
		File f = new File(nomeCompleto);
		try {
			f.createNewFile();
			FileOutputStream saida = new FileOutputStream(f);   
			saida.write(arquivoImagem.getConteudo());   
			saida.close();
		} catch (IOException e) {
			new Exception("Erro ao criar arquivo de imagem para visualização", e);
			e.printStackTrace();
		}
		return caminhoRelativo + "/" + arquivo;
	}

}