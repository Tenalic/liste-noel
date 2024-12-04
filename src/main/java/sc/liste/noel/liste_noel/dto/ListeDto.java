package sc.liste.noel.liste_noel.dto;

import java.io.Serializable;
import java.util.List;

public class ListeDto implements Serializable {

    private java.lang.String nomListe;

    private List<ObjetDto> list;

    private java.lang.String proprietaire;


    public List<ObjetDto> getList() {
        return list;
    }

    public void setList(List<ObjetDto> list) {
        this.list = list;
    }


    public java.lang.String getNomListe() {
        return nomListe;
    }

    public void setNomListe(java.lang.String nomListe) {
        this.nomListe = nomListe;
    }

    public java.lang.String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(java.lang.String proprietaire) {
        this.proprietaire = proprietaire;
    }
}
