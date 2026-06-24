package sc.liste.noel.liste_noel.back.dto.request;

public class CreationListeRequest {

    private String nomListe;
    private Boolean publique;

    public CreationListeRequest() {
    }

    public CreationListeRequest(String nomListe) {
        this.nomListe = nomListe;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }

    public Boolean getPublique() {
        return publique;
    }

    public void setPublique(Boolean publique) {
        this.publique = publique;
    }
}
