package util;

import java.security.Principal;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	public static void invalidarSessao() throws Exception {
		getExternalContext().invalidateSession();
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		getExternalContext().redirect(request.getContextPath());

		/*
		((HttpSession) getExternalContext().getSession(true)).invalidate();
		HttpServletResponse response = (HttpServletResponse) getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
		response.sendRedirect(request.getContextPath());
		*/
	}
	
	/**
	 * Recupera o login do usuário autenticado.
	 * @return
	 */
	public static String getLogin() {
		String login = null;
		Principal p = FacesContext.getCurrentInstance()
				.getExternalContext().getUserPrincipal();
		if (p != null) {
			login = p.getName();
		}
		return login;
	}

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
}
