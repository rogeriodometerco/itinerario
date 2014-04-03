package util;

import java.io.File;
import java.io.FileOutputStream;

import modelo.ArquivoImagem;

public class ArquivoUtil {

	public static ArquivoImagem gravarArquivoImagem(byte[] b, String nomeArquivo) throws Exception {
		String pasta = "c:\\imagensItinerario";
		String arquivo = System.currentTimeMillis() + "_" + nomeArquivo;
		File f = new File(pasta);
		f.mkdirs();
		String nomeCompleto = pasta + "\\" + arquivo;
		f = new File(nomeCompleto);
		FileOutputStream saida = new FileOutputStream(f);   
		saida.write(b);   
		saida.close();
		System.out.println("nome completo do arquivo: " + nomeCompleto);
		ArquivoImagem arquivoImagem = new ArquivoImagem();
		arquivoImagem.setArquivo(f.getAbsolutePath());
		arquivoImagem.setConteudo(b);
		return arquivoImagem;
	}

}