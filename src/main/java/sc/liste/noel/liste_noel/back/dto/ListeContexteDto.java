package sc.liste.noel.liste_noel.back.dto;

public class ListeContexteDto extends ListeDto{

    private boolean estProprietaire;
    private boolean estFavoris;

    public ListeContexteDto() {
    }

    public ListeContexteDto(ListeDto liste) {
        this.setIdListe(liste.getIdListe());
        this.setNomListe(liste.getNomListe());
        this.setProprietaire(liste.getProprietaire());
        this.setListeObjet(liste.getListeObjet());
        this.setUrlPartage(liste.getUrlPartage());
        this.setPseudoProprietaire(liste.getPseudoProprietaire());
        this.setPublique(liste.isPublique());
        this.setNombreObjet(liste.getNombreObjet());
    }

    public boolean isEstProprietaire() {
        return estProprietaire;
    }

    public void setEstProprietaire(boolean estProprietaire) {
        this.estProprietaire = estProprietaire;
    }

    public boolean isEstFavoris() {
        return estFavoris;
    }

    public void setEstFavoris(boolean estFavoris) {
        this.estFavoris = estFavoris;
    }
}
