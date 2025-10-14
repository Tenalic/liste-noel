package sc.liste.noel.liste_noel.common.dto;

import java.io.Serializable;

public class ObjetDto implements Serializable {

    private Long idObjet;

    private String titre;

    private String description;

    private String url;

    private boolean estPrit;

    private String detenteur;

    private String pseudoDetenteur;

    private String priorite;
    private Integer valuePriorite;

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

    public Long getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(Long idObjet) {
        this.idObjet = idObjet;
    }

    public String getPseudoDetenteur() {
        return pseudoDetenteur;
    }

    public void setPseudoDetenteur(String pseudoDetenteur) {
        this.pseudoDetenteur = pseudoDetenteur;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public Integer getValuePriorite() {
        return valuePriorite;
    }

    public void setValuePriorite(Integer valuePriorite) {
        this.valuePriorite = valuePriorite;
    }
}
