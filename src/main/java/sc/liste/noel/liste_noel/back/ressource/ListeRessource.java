package sc.liste.noel.liste_noel.back.ressource;

import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sc.liste.noel.liste_noel.back.dto.request.CreationListeRequest;
import sc.liste.noel.liste_noel.back.dto.response.GeneriqueResponse;
import sc.liste.noel.liste_noel.back.dto.response.ListeReponse;
import sc.liste.noel.liste_noel.back.dto.response.MesListesResponse;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;
import sc.liste.noel.liste_noel.back.dto.ListeDto;
import sc.liste.noel.liste_noel.back.dto.ListeContexteDto;
import sc.liste.noel.liste_noel.back.dto.ObjetDto;
import sc.liste.noel.liste_noel.back.service.MessageService;
import sc.liste.noel.liste_noel.back.Constantes;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import static sc.liste.noel.liste_noel.back.Constantes.API_LISTE_ERREUR_KEY;
import static sc.liste.noel.liste_noel.back.Constantes.API_SECRET_INVALID_KEY;

@RestController
@RequestMapping("/api/liste")
public class ListeRessource {

    private static final Logger LOGGER = LogManager.getLogger(ListeRessource.class);

    private final ListeServiceInterface listeServiceInterface;

    private final MessageService messageService;

    public ListeRessource(ListeServiceInterface listeServiceInterface,
                          MessageService messageService) {
        this.listeServiceInterface = listeServiceInterface;
        this.messageService = messageService;
    }

    @GetMapping("/mes-listes")
    public ResponseEntity<MesListesResponse> getMesListes(Principal principal) {
        String email = principal.getName();
        try {
            List<ListeDto> listes = listeServiceInterface.getListesOfEmail(email);
            List<ListeDto> favoris = listeServiceInterface.getListeFavorisOfEmail(email);
            return ResponseEntity.ok(new MesListesResponse(listes, favoris));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la récupération des listes pour " + email, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{idListe}")
    public ResponseEntity<ListeReponse> getUneListe(Principal principal,
                                                    @PathVariable String idListe) {
        String email = principal != null ? principal.getName() : null;
        try {
            ListeContexteDto liste = listeServiceInterface.getListeAvecContexte(Long.valueOf(idListe), email);
            return ResponseEntity.ok(new ListeReponse("Succes", Constantes.RETOUR_API_OK, liste, liste.isEstProprietaire(), liste.isEstFavoris()));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la récupération de la listes " + idListe, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{idListe}/favoris")
    public ResponseEntity<GeneriqueResponse> addFavoris(Principal principal,
                                                        @PathVariable String idListe) {
        String email = principal.getName();
        try {
            listeServiceInterface.modifierFavori(Long.valueOf(idListe), email);
            return ResponseEntity.ok(new GeneriqueResponse("Succes", Constantes.RETOUR_API_OK));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la modification de favoris {} {}", idListe, email, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/creer")
    public ResponseEntity<GeneriqueResponse> creerUneListe(Principal principal,
                                                           @RequestBody CreationListeRequest listeRequest) {
        String email = principal.getName();
        listeServiceInterface.creerListe(email, listeRequest.getNomListe());
        return ResponseEntity.ok(new GeneriqueResponse("Succes", Constantes.RETOUR_API_OK));
    }

    @DeleteMapping("/{idListe}")
    public ResponseEntity<GeneriqueResponse> supprimerUneListe(
            Locale locale, @PathVariable String idListe,
            Principal principal) {
        String email = principal.getName();
        try {
            String response = listeServiceInterface.supprimerListe(Long.valueOf(idListe));
            return ResponseEntity.ok(new GeneriqueResponse(response, Constantes.RETOUR_API_OK));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression de la liste " + idListe + " pour l'email " + email, e);
            return ResponseEntity.internalServerError().body(new GeneriqueResponse(messageService.getMessage(API_LISTE_ERREUR_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @PostMapping("/{idListe}/cadeau")
    public ResponseEntity<GeneriqueResponse> ajouterObjet(Principal principal,
                                                          @RequestBody ObjetDto objet,
                                                          @PathVariable String idListe,
                                                          Locale locale) {
        String email = principal.getName();
        LOGGER.info("Ajout de l'objet {} par l'utilisateur {}", objet.getTitre(), email);
        try {
            listeServiceInterface.ajouterObjetDansUneListe(objet.getTitre(), objet.getUrl(), objet.getDescription(), idListe, email, objet.getValuePriorite());
            return ResponseEntity.ok(new GeneriqueResponse("Succes", Constantes.RETOUR_API_OK));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GeneriqueResponse(messageService.getMessage(API_LISTE_ERREUR_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }
}
