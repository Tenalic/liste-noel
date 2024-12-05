package sc.liste.noel.liste_noel.dto;

import java.io.Serializable;

public class ObjetDto implements Serializable {

    private String titre;

    private String description;

    private String url;

    private boolean estPrit;

    private String detenteur;

    public ObjetDto() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
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
