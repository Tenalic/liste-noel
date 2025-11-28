package sc.liste.noel.liste_noel.back.ressource;

import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sc.liste.noel.liste_noel.back.dto.GeneriqueResponse;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;
import sc.liste.noel.liste_noel.common.service.MessageService;
import sc.liste.noel.liste_noel.front.constante.Constantes;

import java.util.Locale;

import static sc.liste.noel.liste_noel.front.constante.Constantes.API_LISTE_ERREUR_KEY;
import static sc.liste.noel.liste_noel.front.constante.Constantes.API_SECRET_INVALID_KEY;

@RestController
@RequestMapping("/liste")
public class ListeRessource {

    private static final Logger LOGGER = LogManager.getLogger(ListeRessource.class);

    private final ListeServiceInterface listeServiceInterface;

    private final SecretServiceInterface secretService;

    private final MessageService messageService;

    public ListeRessource(ListeServiceInterface listeServiceInterface, SecretServiceInterface secretService, MessageService messageService) {
        this.listeServiceInterface = listeServiceInterface;
        this.secretService = secretService;
        this.messageService = messageService;
    }

    @DeleteMapping("supprimer-liste")
    public ResponseEntity<GeneriqueResponse> supprimerListe(
            @RequestParam @NotBlank String email,
            @RequestParam @NotBlank String nomListe,
            @RequestHeader(value = "secret") String secret,
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "fr") Locale locale) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GeneriqueResponse(messageService.getMessage(API_SECRET_INVALID_KEY, locale), Constantes.RETOUR_API_KO));
            }
            String response = listeServiceInterface.supprimerListe(nomListe, email);
            return ResponseEntity.ok(new GeneriqueResponse(response, Constantes.RETOUR_API_OK));
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression de la liste " + nomListe + " pour l'email " + email, e);
            return ResponseEntity.internalServerError().body(new GeneriqueResponse(messageService.getMessage(API_LISTE_ERREUR_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }
}
