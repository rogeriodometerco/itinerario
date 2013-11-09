package managedbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.POST;

import modelo.Linha;
import modelo.PontoLinha;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import util.JsfUtil;
import facade.LinhaFacade;

@ManagedBean
@ViewScoped
public class LinhaMb_bkp {

	@EJB
	private LinhaFacade facade;

	private Linha linha;
	private List<Linha> lista;

	private UploadedFile arquivo;
	private MapModel mapModel;
	private String centroMapa;
	private Integer zoomMapa;
	@EJB
	private LinhaFacade linhaFacade;

	@PostConstruct
	private void inicializar() {
		//prepararNova();
		resetarMapa();
	}
	
	public Linha getLinha() {
		return linha;
	}

	public String prepararNova() {
		novaLinha();
		return "cadastroLinha";
	}
	
	private void novaLinha() {
		this.linha = new Linha();
		linha.setPontos(new ArrayList<PontoLinha>());
		resetarMapa();
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public List<Linha> listar() {
		try {
			lista = facade.listar();
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
		}
		return lista;
	}

	public List<Linha> getLista() {
		return lista;
	}

	public String salvar() {
		try {
			facade.salvar(linha);
			this.linha = null;
			JsfUtil.addMsgSucesso("Informações salvas com sucesso.");
			return "listaLinha";
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
		}
		return null;
	}

	public String cancelar() {
		this.linha = null;
		return "listaLinha";
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}
	
	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void arquivoImportado(FileUploadEvent event) {  
		this.arquivo = event.getFile();
		mapModel = new DefaultMapModel();
		criarPontos();
		resetarMapa();
		criarLinhas();
		criarMarcadores();
		System.out.println("arquivoImportado() - " + mapModel.getMarkers().size() + " marcadores.");
		JsfUtil.addMsgSucesso(arquivo.getFileName() 
				+ " carregado. " + linha.getPontos().size() + " pontos importados.");
	}  

	private void criarMarcadores() {
		for (PontoLinha ponto: linha.getPontos()) {
			criarMarcador(ponto);
		}
	}

	private void criarMarcador(PontoLinha pontoLinha) {
		String icone = "mm_20_white.png";

		LatLng latLng = new LatLng(pontoLinha.getLat(), 
				pontoLinha.getLng());
		Marker marker = new Marker(latLng, "", pontoLinha);
		marker.setIcon("resources/icones/" + icone);
		String titulo = marker.getLatlng().toString()
				+ "\nLinha: "
				+ pontoLinha.getLinha().getNome()
				+ "\nSequencia: " 
				+ pontoLinha.getSequencia();
		marker.setTitle(titulo);
		this.mapModel.addOverlay(marker);

	}

	private void criarLinhas() {
		Polyline polyline = new Polyline();
		for (PontoLinha ponto: linha.getPontos()) {
			LatLng latLng = new LatLng(ponto.getLat(), ponto.getLng());
			polyline.getPaths().add(latLng);
		}
		polyline.setStrokeWeight(7);  
		polyline.setStrokeColor("#0000FF");  
		polyline.setStrokeOpacity(0.3);  
		mapModel.addOverlay(polyline);  
	}

	private void criarPontos() {
		if (linha == null) { 
			linha = new Linha();
		}
		linha.setPontos(new ArrayList<PontoLinha>());

		int cont = 1;
		try {
			Scanner s = new Scanner(arquivo.getInputstream());
			String[] linhaArquivo = null;
			Object[] array = null;
			PontoLinha pontoLinha = null;
			while (s.hasNext()) {
				linhaArquivo = s.nextLine().split(",");
				pontoLinha = new PontoLinha();
				pontoLinha.setLinha(this.linha);

				array = extrairDados(linhaArquivo);
				pontoLinha.setLat((Double)array[0]);
				pontoLinha.setLng((Double)array[1]);
				pontoLinha.setSequencia(cont);
				linha.getPontos().add(pontoLinha);
				cont++;
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao importar arquivo. Linha " + cont + ". " + e.getMessage());
			e.printStackTrace();
		}
	}

	// TODO Implementar classe extratora de dados do arquivo.
	private Object[] extrairDados(String[] linhaArquivo) {
		// formato da linha do arquivo:
		// $GPRMC,114938.000,A,2445.1896,S,05145.6836,W,7.58,130.84,261013,,,A*6B
		// ?      hora       ? lat         lng          vel. ?      data 

		Object[] array = new Object[2];
		String latStr = linhaArquivo[3];
		double graus = Double.parseDouble(latStr.substring(0,  2));
		double minutos = Double.parseDouble(latStr.substring(2)) / 60;
		array[0] = (graus + minutos) * -1;

		String lngStr = linhaArquivo[5];
		graus = Double.parseDouble(lngStr.substring(0,  3));
		minutos = Double.parseDouble(lngStr.substring(3)) / 60;
		array[1] = (graus + minutos) * -1;

		return array;
	}

	public void onStateChange(StateChangeEvent event) {
		zoomMapa = event.getZoomLevel();
		centroMapa = event.getCenter().getLat() + ", " + event.getCenter().getLng();
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public String getCentroMapa() {
		return centroMapa;
	}

	public Integer getZoomMapa() {
		return zoomMapa;
	}

	private void resetarMapa() {
		System.out.println("resetarMapa()");
		mapModel = new DefaultMapModel();
		if (linha == null || linha.getPontos().size() == 0) {
			this.centroMapa = "-24.750573, -51.781526";
		} else {
			this.centroMapa = 
					linha.getPontos().get(0).getLat() 
					+ ", " + linha.getPontos().get(0).getLng();
		} 
		this.zoomMapa = 10;
	}

}