package managedbean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import modelo.Linha;
import facade.LinhaFacade;

@ManagedBean
@ViewScoped
public class DogMB {

	@EJB
	private LinhaFacade linhaFacade;
	
	private static final String CREATE_DOG = "createDog";
	private static final String DELETE_DOG = "deleteDog"; 
	private static final String UPDATE_DOG = "updateDog";
	private static final String LIST_ALL_DOGS = "listAllDogs";
	private static final String STAY_IN_THE_SAME_PAGE = null;

	private Linha linha;

	public Linha getDog() {
		
		if(linha == null){
			linha = new Linha();
		}
		
		return linha;
	}

	public void setDog(Linha dog) {
		System.out.println("setDog()");
		this.linha = dog;
	}

	public List<Linha> getAllDogs() {
		try {
		return linhaFacade.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String updateDogStartTeste(Linha dog){
		this.linha = dog;
		return UPDATE_DOG;
	}
	
	public String updateDogStart(){
		return UPDATE_DOG;
	}
	
	public String updateDogEnd(){
		try {
			linhaFacade.salvar(linha);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Check if the weight is above 0 or call the adm");
			return STAY_IN_THE_SAME_PAGE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sendInfoMessageToUser("Operation Complete: Update");
		return LIST_ALL_DOGS;
	}
	
	public String deleteDogStart(){
		return DELETE_DOG;
	}
	
	public String deleteDogEnd(){
		try {
			linhaFacade.excluir(linha);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Call the ADM");
			return STAY_IN_THE_SAME_PAGE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sendInfoMessageToUser("Operation Complete: Delete");
		
		return LIST_ALL_DOGS;
	}
	
	public String createDogStart(){
		return CREATE_DOG;
	}
	
	public String createDogEnd(){
		try {
			linhaFacade.salvar(linha);
		} catch (EJBException e) {
			sendErrorMessageToUser("Error. Check if the weight is above 0 or call the adm");
			
			return STAY_IN_THE_SAME_PAGE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sendInfoMessageToUser("Operation Complete: Create");
		
		return LIST_ALL_DOGS;
	}
	
	public String listAllDogs(){
		return LIST_ALL_DOGS;
	}
	
	private void sendInfoMessageToUser(String message){
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}
	
	private void sendErrorMessageToUser(String message){
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}
	
	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}
}