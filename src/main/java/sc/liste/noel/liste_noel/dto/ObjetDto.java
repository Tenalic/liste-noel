package sc.liste.noel.liste_noel.dto;

import java.io.Serializable;

public class ObjetDto implements Serializable {

    private java.lang.String titre;

    private java.lang.String description;

    private java.lang.String url;

    private boolean estPrit;

    private String detenteur;

    public ObjetDto() {
    }

    public java.lang.String getTitre() {
        return titre;
    }

    public void setTitre(java.lang.String titre) {
        this.titre = titre;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getUrl() {
        return url;
    }

    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public boolean isEstPrit() {
        return estPrit;
    }

    public void setEstPrit(boolean estPrit) {
        this.estPrit = estPrit;
    }

    public String getDetenteur() {
        return detenteur;
    }

    public void setDetenteur(String detenteur) {
        this.detenteur = detenteur;
    }
}
