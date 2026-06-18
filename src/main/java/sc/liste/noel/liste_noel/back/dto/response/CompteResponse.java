package sc.liste.noel.liste_noel.back.dto.response;

public class CompteResponse extends GeneriqueResponse {

    private static final long serialVersionUID = 1L;

    private String email;

    private String pseudo;

    public CompteResponse() {
    }

    public CompteResponse(String email) {
        this.email = email;
    }

    public CompteResponse(String email, String messageRetour, int codeRetour) {
        super(messageRetour, codeRetour);
        this.email = email;
    }

    public CompteResponse(String email,String pseudo, String messageRetour, int codeRetour) {
        super(messageRetour, codeRetour);
        this.email = email;
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
