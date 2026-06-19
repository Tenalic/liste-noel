package sc.liste.noel.liste_noel.back.service;

import sc.liste.noel.liste_noel.back.exception.ListeNotFoundException;
import sc.liste.noel.liste_noel.back.exception.ModificationInterditeException;
import sc.liste.noel.liste_noel.back.dto.ListeDto;
import sc.liste.noel.liste_noel.back.dto.ListeContexteDto;

import java.util.List;

public interface ListeServiceInterface {

    ListeDto creerListe(String proprietaire, String nomListe, boolean publique);

    List<ListeDto> getListesOfEmail(String email);

    ListeDto getListeById(Long id) throws ListeNotFoundException;

    List<ListeDto> getListes(boolean publique, String nomListe) throws ListeNotFoundException;

    void updatePublique(Long idListe, boolean publique, String email) throws ModificationInterditeException, ListeNotFoundException;

    void ajouterObjetDansUneListe(String titre, String url, String description, String idListe, String proprietaire, int priorite);

    void prendreUnObjet(String idListe, String idObjet, String personne, String pseudo);

    void nePlusPrendreUnObjet(String idObjet);

    List<ListeDto> getListeFavorisOfEmail(String email);

    boolean checkifListeInFavoris(Long idListe, String email);

    void ajouterFavori(Long idListe, String email);

    void supprimerFavori(Long idListe, String email);

    void modifierFavori(Long idListe, String email);

    void supprimerObjet(Long idObjet, String email) throws ModificationInterditeException;

    void modifierObjet(Long idObjet, String titreUpdate, String descriptionUpdate, String urlUpdate, int prioriteUpdate, String email) throws ModificationInterditeException;

    String supprimerListe(String nomListe, String emailListe);

    String supprimerListe(Long idListe);

    ListeContexteDto getListeAvecContexte(Long id, String email) throws ListeNotFoundException;

}
