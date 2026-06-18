package sc.liste.noel.liste_noel.back.ressource;


import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sc.liste.noel.liste_noel.back.dto.request.ConnexionRequest;
import sc.liste.noel.liste_noel.back.dto.request.InscriptionRequest;
import sc.liste.noel.liste_noel.back.dto.request.ModifierMotDePasseRequest;
import sc.liste.noel.liste_noel.back.dto.request.MotDePasseOublieRequest;
import sc.liste.noel.liste_noel.back.dto.response.CompteResponse;
import sc.liste.noel.liste_noel.back.dto.response.GeneriqueResponse;
import sc.liste.noel.liste_noel.back.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.back.exception.MailServiceDesactivedException;
import sc.liste.noel.liste_noel.back.exception.MotDePasseException;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.back.service.JwtService;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;
import sc.liste.noel.liste_noel.back.dto.CompteDto;
import sc.liste.noel.liste_noel.back.service.MessageService;
import sc.liste.noel.liste_noel.back.Constantes;

import java.security.Principal;
import java.time.Duration;
import java.util.Locale;

import static sc.liste.noel.liste_noel.back.Constantes.*;

@RestController
@RequestMapping("/api/compte")
public class CompteRessource {

    private static final Logger LOGGER = LogManager.getLogger(CompteRessource.class);

    @Autowired
    private CompteServiceInterface compteService;
    @Autowired
    private SecretServiceInterface secretService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private JwtService jwtService;

    /**
     * API permettant de créer un nouveau compte.
     *
     * @param locale Locale pour la langue des messages (optionnel, défaut = fr).
     * @return ResponseEntity<CompteResponse> avec le code retour (0 si OK) et un message.
     */
    @PostMapping("/inscription")
    public ResponseEntity<CompteResponse> inscription(
            @RequestBody InscriptionRequest inscriptionRequest,
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "fr")
            Locale locale) {
        try {
            // Vérification de l'existence du compte
            if (compteService.compteExiste(inscriptionRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CompteResponse(inscriptionRequest.getEmail(), messageService.getMessage(COMPTE_EXISTE_KEY, locale), Constantes.RETOUR_API_KO));
            }

            // Vérification de l'existence du pseudo
            if (compteService.pseudoExiste(inscriptionRequest.getPseudo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CompteResponse(inscriptionRequest.getEmail(), messageService.getMessage(PSEUDO_EXISTE_KEY, locale), Constantes.RETOUR_API_KO));
            }

            // Création du compte
            String email = compteService.creationCompte(inscriptionRequest.getEmail(), inscriptionRequest.getPassword(), inscriptionRequest.getAcceptCGU(), inscriptionRequest.getPseudo());

            String token = jwtService.genererToken(email);

            // Cookie HTTP-only : JavaScript ne peut PAS le lire → sécurisé contre XSS
            ResponseCookie cookie = ResponseCookie.from("auth-token", token)
                    .httpOnly(true)                     // inaccessible depuis JS
                    .secure(true)                       // HTTPS uniquement (false en dev local)
                    .path("/")                          // valable sur toutes les routes
                    .maxAge(Duration.ofSeconds(86400))  // 24h, comme jwt.expiration
                    .sameSite("Strict")                 // protection CSRF
                    .build();


            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new CompteResponse(inscriptionRequest.getEmail(), messageService.getMessage(Constantes.API_COMPTE_CREATION_SUCCES_KEY, locale), Constantes.RETOUR_API_OK));

        } catch (Exception e) {
            LOGGER.error("Erreur lors de la création du compte pour l'email : " + inscriptionRequest.getEmail(), e);
            return ResponseEntity.internalServerError().body(new CompteResponse(inscriptionRequest.getEmail(), messageService.getMessage(COMPTE_ERROR_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<CompteResponse> connexion(@RequestBody @Valid ConnexionRequest connexionRequest,
                                                    Locale locale) {
        String email = connexionRequest.getEmail();

        LOGGER.info("Entrée service connexion : " + email);

        try {
            CompteDto compte = compteService.connexion(email, connexionRequest.getPassword());

            String token = jwtService.genererToken(compte.getEmail());

            // Cookie HTTP-only : JavaScript ne peut PAS le lire → sécurisé contre XSS
            ResponseCookie cookie = ResponseCookie.from("auth-token", token)
                    .httpOnly(true)                     // inaccessible depuis JS
                    .secure(true)                       // HTTPS uniquement (false en dev local)
                    .path("/")                          // valable sur toutes les routes
                    .maxAge(Duration.ofSeconds(86400))  // 24h, comme jwt.expiration
                    .sameSite("Strict")                 // protection CSRF
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new CompteResponse(compte.getEmail(), "Connexion réussie", RETOUR_API_OK));

        } catch (CompteNotFoundException exception) {
            return ResponseEntity.ok()
                    .body(new CompteResponse(email, messageService.getMessage(CONNEXION_FAIL_KEY, locale)
                            , RETOUR_API_KO));

        } catch (Exception e) {
            LOGGER.error("Erreur lors de la connexion : " + email, e);
            return ResponseEntity.internalServerError()
                    .body(new CompteResponse(email, messageService.getMessage(API_ERROR_GENERIC_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @PostMapping("/deconnexion")
    public ResponseEntity<Void> deconnexion() {
        // On écrase le cookie avec maxAge=0 → le navigateur le supprime immédiatement
        ResponseCookie cookie = ResponseCookie.from("auth-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)          // ← c'est ça qui supprime le cookie
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/mot-de-passe-oublie")
    public ResponseEntity<GeneriqueResponse> motDePasseOublie(@RequestBody MotDePasseOublieRequest motDePasseOublieRequest, Locale locale) {
        try {
            compteService.genererMotDePasseEtEnvoyer(motDePasseOublieRequest.getEmail());
            return ResponseEntity.ok().body(new GeneriqueResponse(messageService.getMessage(MOT_DE_PASSE_OUBLIE_P1_KEY, locale) + motDePasseOublieRequest.getEmail()
                    + " " + messageService.getMessage(MOT_DE_PASSE_OUBLIE_P2_KEY, locale), Constantes.RETOUR_API_OK));
        } catch (MailServiceDesactivedException e) {
            LOGGER.warn("L'envois d'email est désactivé, le mot de passe pour le compte {} n\'a pas été généré", motDePasseOublieRequest.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GeneriqueResponse(messageService.getMessage(EMAIL_DESACTIVETED, locale), Constantes.RETOUR_API_KO));
        } catch (Exception e) {
            LOGGER.error("[mot-de-passe-oublie] Une erreur est survenu", e);
            return ResponseEntity.internalServerError()
                    .body(new GeneriqueResponse(messageService.getMessage(API_ERROR_GENERIC_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<CompteResponse> updateAPassword(@RequestBody ModifierMotDePasseRequest modifierMotDePasseRequest,
                                                          Principal principal,
                                                          Locale locale) {
        String email = principal.getName();

        try {
            compteService.updatePassword(email, modifierMotDePasseRequest.getAncienMotDePasse(), modifierMotDePasseRequest.getNouveauMotDePasse(), modifierMotDePasseRequest.getConfirmationMotDePasse());
            return ResponseEntity.ok(new CompteResponse(email, messageService.getMessage(API_COMPTE_PASSWORD_UPDATE_SUCCES_KEY, locale), Constantes.RETOUR_API_OK));
        } catch (CompteNotFoundException exception) {
            LOGGER.warn("[update-password] Compte introuvable, le mot de passe ne doit pas être correcte pour l'email : {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CompteResponse(email, messageService.getMessage(MDP_CHANGE_INTROUVABLE, locale), Constantes.RETOUR_API_KO));
        } catch (MotDePasseException exception) {
            LOGGER.warn("[update-password] Les mots de passe ne sont pas identique pour l'email {}", email);
            return ResponseEntity.badRequest()
                    .body(new CompteResponse(email, messageService.getMessage(MDP_CHANGE_INCORECTE, locale), Constantes.RETOUR_API_KO));
        } catch (Exception e) {
            LOGGER.error("[update-password] Erreur lors de la mise à jour du mot de passe pour le email : {}", email, e);
            return ResponseEntity.internalServerError()
                    .body(new CompteResponse(email, messageService.getMessage(API_ERROR_GENERIC_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    /**
     * API permetant de supprimer un compte
     *
     * @param email   : email du joueur
     * @param secret: secret de l'application appelante afin de s'authentifier
     * @return ResponseEntity<CompteResponse> contenant le email, le code retour (0
     * si ok) et le messegae retour
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<CompteResponse> supprimerCompte(
            @RequestParam @NotBlank String email,
            @RequestHeader(value = "secret") String secret,
            Locale locale) {

        try {
            if (!secretService.verifierSecret(secret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CompteResponse(email, messageService.getMessage(API_SECRET_INVALID_KEY, locale), Constantes.RETOUR_API_KO));
            }

            if (!compteService.supprimerCompte(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new CompteResponse(email, messageService.getMessage(API_COMPTE_SUPPRESSION_ECHEC_KEY, locale), Constantes.RETOUR_API_KO));
            }

            return ResponseEntity.ok(new CompteResponse(email, messageService.getMessage(API_COMPTE_SUPPRESSION_SUCCES_KEY, locale), Constantes.RETOUR_API_OK));

        } catch (Exception e) {
            LOGGER.error("Erreur lors de la suppression du compte pour l'email : {}", email, e);
            return ResponseEntity.internalServerError()
                    .body(new CompteResponse(email, messageService.getMessage(API_ERROR_GENERIC_KEY, locale), Constantes.RETOUR_API_KO));
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(
            @RequestParam @NotBlank String email,
            @RequestParam @NotBlank String key,
            Locale locale) {

        boolean activated = compteService.activateUser(email, key);
        if (activated) {
            return ResponseEntity.ok(messageService.getMessage(API_COMPTE_ACTIVATION_SUCCES_KEY, locale));
        } else {
            return ResponseEntity.badRequest().body(messageService.getMessage(API_COMPTE_ACTIVATION_ECHEC_KEY, locale));
        }
    }

}
