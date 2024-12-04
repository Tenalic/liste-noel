package sc.liste.noel.liste_noel.service;

import sc.liste.noel.liste_noel.dto.ListeDto;

import java.util.List;

public interface ListeServiceInterface {

    ListeDto creerListe(String proprietaire, String nomListe);

    List<ListeDto> getListeOfEmail(String email);

}
