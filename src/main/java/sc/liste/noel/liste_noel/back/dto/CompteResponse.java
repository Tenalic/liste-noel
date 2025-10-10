package sc.liste.noel.liste_noel.back.dto;

import java.io.Serializable;

public class CompteResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;

	private String messageRetour;

	private int codeRetour;

	public CompteResponse() {
	}

	public CompteResponse(String email) {
		this.email = email;
	}

	public CompteResponse(String email, String messageRetour, int codeRetour) {
		super();
		this.email = email;
		this.messageRetour = messageRetour;
		this.codeRetour = codeRetour;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessageRetour() {
		return messageRetour;
	}

	public void setMessageRetour(String messageRetour) {
		this.messageRetour = messageRetour;
	}

	public int getCodeRetour() {
		return codeRetour;
	}

	public void setCodeRetour(int codeRetour) {
		this.codeRetour = codeRetour;
	}

}
