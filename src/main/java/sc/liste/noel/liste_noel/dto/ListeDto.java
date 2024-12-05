package sc.liste.noel.liste_noel.dto;

import java.io.Serializable;
import java.util.List;

public class ListeDto implements Serializable {
    
    private Long idListe;

    private String nomListe;

    private List<ObjetDto> listeObjet;

    private String proprietaire;


    public List<ObjetDto> getListeObjet() {
        return listeObjet;
    }

    public void setListeObjet(List<ObjetDto> listeObjet) {
        this.listeObjet = listeObjet;
    }


    public String getNomListe() {
        return nomListe;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Long getIdListe() {
        return idListe;
    }

    public void setIdListe(Long idListe) {
        this.idListe = idListe;
    }
}
