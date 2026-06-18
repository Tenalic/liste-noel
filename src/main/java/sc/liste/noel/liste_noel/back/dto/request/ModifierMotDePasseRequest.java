package sc.liste.noel.liste_noel.back.dto.request;

public class ModifierMotDePasseRequest {

    private String ancienMotDePasse;
    private String nouveauMotDePasse;
    private String confirmationMotDePasse;

    public ModifierMotDePasseRequest() {
    }

    public ModifierMotDePasseRequest(String ancienMotDePasse, String nouveauMotDePasse, String confirmationMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
        this.nouveauMotDePasse = nouveauMotDePasse;
        this.confirmationMotDePasse = confirmationMotDePasse;
    }

    public String getAncienMotDePasse() {
        return ancienMotDePasse;
    }

    public void setAncienMotDePasse(String ancienMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
    }

    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    public String getConfirmationMotDePasse() {
        return confirmationMotDePasse;
    }

    public void setConfirmationMotDePasse(String confirmationMotDePasse) {
        this.confirmationMotDePasse = confirmationMotDePasse;
    }
}
