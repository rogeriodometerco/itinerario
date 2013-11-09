package util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class JsfUtil {
	/*
	public static void msgSucesso(String chaveMsg) {
		String msg = getMsgLocal(chaveMsg);
		addMsgSucesso (msg);
	}
	public static void msgErro(String chaveMsg) {
		String msg = getMsgLocal(chaveMsg);
		addMsgErro (msg);
	}
	public static void msgErro(Exception ex, String chaveMsgDefault) {
		String msg = ex.getLocalizedMessage();
		if (msg.isEmpty()) {
			msg = getMsgLocal(chaveMsgDefault);
		}
		addMsgErro(msg);
	}
	public static String getMsgLocal (String chaveMsg) {
		return ResourceBundle.getBundle("com.servicompu.cotacao.web.mensagens.Mensagens").getString(chaveMsg);
	}
	*/	
	public static void addMsgSucesso(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}
	public static void addMsgErro(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}
	
}
