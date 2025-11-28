package sc.liste.noel.liste_noel.front.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.dto.GeneriqueResponse;
import sc.liste.noel.liste_noel.back.ressource.ListeRessource;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;
import sc.liste.noel.liste_noel.front.mappers.GeneriqueResponseMapper;

import java.util.Locale;

@Service
public class ListeService {

    private final ListeRessource listeRessource;
    private final SecretServiceInterface secretService;

    public ListeService(ListeRessource listeRessource, SecretServiceInterface secretService) {
        this.listeRessource = listeRessource;
        this.secretService = secretService;
    }

    public sc.liste.noel.liste_noel.front.dtos.GeneriqueResponse supprimerListe(String email, String nomListe, Locale locale) {
        ResponseEntity<GeneriqueResponse> generiqueResponseResponseEntity = listeRessource.supprimerListe(email, nomListe, secretService.getMySecret(), locale);
        return GeneriqueResponseMapper.dtoBackToDtoFront(generiqueResponseResponseEntity.getBody());
    }
}
