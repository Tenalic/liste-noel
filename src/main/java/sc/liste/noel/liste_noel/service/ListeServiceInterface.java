package sc.liste.noel.liste_noel.service;

import sc.liste.noel.liste_noel.dto.ListeDto;

import java.util.List;

public interface ListeServiceInterface {

    ListeDto creerListe(String proprietaire, String nomListe);

    List<ListeDto> getListeOfEmail(String email);

    ListeDto getListeById(Long id);

    void ajouterObjet(String titre, String url, String description, String idListe, String proprietaire);

    void prendreUnObjet(String idListe, String idObjet, String personne, String pseudo);
    void nePlusPrendreUnObjet(String idObjet);

    List<ListeDto> getFavorisList(String email);

    void ajouterFavoris(Long idListe, String email);


}
