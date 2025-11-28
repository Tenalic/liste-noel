package sc.liste.noel.liste_noel.back.dto;

public class CompteResponse extends GeneriqueResponse {

	private static final long serialVersionUID = 1L;

	private String email;

	public CompteResponse() {
	}

	public CompteResponse(String email) {
		this.email = email;
	}

	public CompteResponse(String email, String messageRetour, int codeRetour) {
        super(messageRetour, codeRetour);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
