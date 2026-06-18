package sc.liste.noel.liste_noel.back.dto.response;

import sc.liste.noel.liste_noel.back.dto.ListeDto;

public class ListeReponse extends GeneriqueResponse{

    private ListeDto listeCadeaux;
    private boolean estProprietaire;
    private boolean estEnFavoris;

    public ListeReponse(String messageRetour, int codeRetour, ListeDto listeCadeaux) {
        super(messageRetour, codeRetour);
        this.listeCadeaux = listeCadeaux;
    }

    public ListeReponse(String messageRetour, int codeRetour, ListeDto listeCadeaux, boolean estProprietaire, boolean estEnFavoris) {
        super(messageRetour, codeRetour);
        this.listeCadeaux = listeCadeaux;
        this.estProprietaire = estProprietaire;
        this.estEnFavoris = estEnFavoris;
    }

    public ListeDto getListeCadeaux() {
        return listeCadeaux;
    }

    public void setListeCadeaux(ListeDto listeCadeaux) {
        this.listeCadeaux = listeCadeaux;
    }

    public boolean isEstProprietaire() {
        return estProprietaire;
    }

    public void setEstProprietaire(boolean estProprietaire) {
        this.estProprietaire = estProprietaire;
    }

    public boolean isEstEnFavoris() {
        return estEnFavoris;
    }

    public void setEstEnFavoris(boolean estEnFavoris) {
        this.estEnFavoris = estEnFavoris;
    }
}
