package sc.liste.noel.liste_noel.back.ressource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sc.liste.noel.liste_noel.back.dto.response.GeneriqueResponse;
import sc.liste.noel.liste_noel.back.exception.ModificationInterditeException;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.back.dto.ObjetDto;
import sc.liste.noel.liste_noel.back.service.MessageService;
import sc.liste.noel.liste_noel.back.mapper.Constantes;

import java.security.Principal;
import java.util.Locale;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static sc.liste.noel.liste_noel.back.mapper.Constantes.API_LISTE_ERREUR_KEY;

@RestController
@RequestMapping("/api/cadeau")
public class ObjetRessource {

    private static final Logger LOGGER = LogManager.getLogger(ObjetRessource.class);

    private final ListeServiceInterface listeServiceInterface;

    private final MessageService messageService;

    public ObjetRessource(ListeServiceInterface listeServiceInterface,
                          MessageService messageService) {
        this.listeServiceInterface = listeServiceInterface;
        this.messageService = messageService;
    }


    @PutMapping("/{idObjet}")
    public ResponseEntity<GeneriqueResponse> modifierObjet(Principal principal,
                                                           @RequestBody ObjetDto objet,
                                                           @PathVariable String idObjet,
                                                           Locale locale) {
        String email = principal.getName();
        LOGGER.info("Modification de l'objet {} par l'utilisateur {}", idObjet, email);
        try {
            listeServiceInterface.modifierObjet(Long.valueOf(idObjet), objet.getTitre(), objet.getDescription(), objet.getUrl(), objet.getValuePriorite(), email);
            return ResponseEntity.ok(new GeneriqueResponse("Succes", Constantes.RETOUR_API_OK));
        } catch (ModificationInterditeException exception) {
            LOGGER.warn("Tentative de modificaiton interdite de l'objet {} par la personne {}", idObjet, email);
            return ResponseEntity.status(FORBIDDEN).body(new GeneriqueResponse(exception.getMessage(), Constantes.RETOUR_API_KO));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GeneriqueResponse(messageService.getMessage(API_LISTE_ERREUR_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @DeleteMapping("/{idObjet}")
    public ResponseEntity<GeneriqueResponse> supprimerCadeau(Principal principal,
                                                             @PathVariable String idObjet,
                                                             Locale locale) {
        String email = principal.getName();
        try {
            listeServiceInterface.supprimerObjet(Long.valueOf(idObjet), email);
            return ResponseEntity.ok().body(new GeneriqueResponse("Succes", Constantes.RETOUR_API_OK));
        } catch (ModificationInterditeException e) {
            LOGGER.warn("Tentative de suppression interdite de l'objet {} par la personne {}", idObjet, email);
            return ResponseEntity.status(FORBIDDEN).body(new GeneriqueResponse(e.getMessage(), Constantes.RETOUR_API_KO));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GeneriqueResponse(messageService.getMessage(API_LISTE_ERREUR_KEY, locale), Constantes.RETOUR_API_KO));
        }

    }
}
