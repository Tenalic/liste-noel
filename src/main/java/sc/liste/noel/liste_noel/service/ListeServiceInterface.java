package sc.liste.noel.liste_noel.service;

import sc.liste.noel.liste_noel.dto.ListeDto;

import java.util.List;

public interface ListeServiceInterface {

    ListeDto creerListe(String proprietaire, String nomListe);

    List<ListeDto> getListesOfEmail(String email);

    ListeDto getListeById(Long id);

    void ajouterObjetDansUneListe(String titre, String url, String description, String idListe, String proprietaire, int priorite);

    void prendreUnObjet(String idListe, String idObjet, String personne, String pseudo);
    void nePlusPrendreUnObjet(String idObjet);

    List<ListeDto> getListeFavorisOfEmail(String email);

    boolean checkifListeInFavoris(Long idListe, String email);

    void ajouterFavori(Long idListe, String email);
    void supprimerFavori(Long idListe, String email);

    void supprimerObjet(Long idObjet, String email);
    void modifierObjet(Long idObjet, String titreUpdate, String descriptionUpdate, String urlUpdate, int prioriteUpdate);


}
