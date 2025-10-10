package sc.liste.noel.liste_noel.db.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "objet")
public class ObjetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objet")
    private Long idObjet;
    @Column(name = "id_liste")
    private Long idListe;
    @Column(name = "titre")
    private String titre;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;
    @Column(name = "est_prit")
    private Boolean estPrit;
    @Column(name = "detenteur")
    private String detenteur;
    @Column(name = "pseudo_detenteur")
    private String pseudoDetenteur;
    @Column(name = "priorite", nullable = false)
    private Integer prioriteValue;

    public ObjetEntity() {
    }

    public Long getIdObjet() {
        return idObjet;
    }

    public void setIdObjet(Long idObjet) {
        this.idObjet = idObjet;
    }

    public Long getIdListe() {
        return idListe;
    }

    public void setIdListe(Long idListe) {
        this.idListe = idListe;
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

    public Boolean getEstPrit() {
        return estPrit;
    }

    public void setEstPrit(Boolean estPrit) {
        this.estPrit = estPrit;
    }

    public String getDetenteur() {
        return detenteur;
    }

    public void setDetenteur(String detenteur) {
        this.detenteur = detenteur;
    }

    public String getPseudoDetenteur() {
        return pseudoDetenteur;
    }

    public void setPseudoDetenteur(String pseudoDetenteur) {
        this.pseudoDetenteur = pseudoDetenteur;
    }

    public Integer getPrioriteValue() {
        return prioriteValue;
    }

    public void setPrioriteValue(Integer prioriteValue) {
        this.prioriteValue = prioriteValue;
    }
}
