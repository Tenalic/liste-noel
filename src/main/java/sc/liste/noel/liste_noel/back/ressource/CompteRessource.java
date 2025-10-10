package sc.liste.noel.liste_noel.back.ressource;



import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sc.liste.noel.liste_noel.front.Utile.Utils;
import sc.liste.noel.liste_noel.front.constante.Constantes;
import sc.liste.noel.liste_noel.back.dto.CompteResponse;
import sc.liste.noel.liste_noel.back.dto.ConnexionResponse;
import sc.liste.noel.liste_noel.front.dto.TokenDto;
import sc.liste.noel.liste_noel.front.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.front.exception.TokenExpiredException;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;

@RestController
@RequestMapping("/compte")
public class CompteRessource {

    private static final Logger LOGGER = LogManager.getLogger(CompteRessource.class);

    @Autowired
    private CompteServiceInterface compteService;

    @Autowired
    private SecretServiceInterface secretService;

    /**
     * API permetant de creer un nouveau compte
     *
     * @param email    : email du joueur
     * @param password : mot de passe du joueur
     * @param langue   : langue pour les messages retours
     * @param secret   : secret de l'application appelante afin de s'authentifier
     * @return ResponseEntity<CompteResponse> contenant le email, le code retour (0
     * si ok) et le messegae retour
     */
    @PostMapping("/creer")
    public ResponseEntity<CompteResponse> creerCompte(@RequestParam(value = "email", required = true) String email,
                                                      @RequestParam(value = "password", required = true) String password,
                                                      @RequestHeader(value = "secret", required = true) String secret,
                                                      @RequestHeader(value = "langue", defaultValue = "1") Integer langue,
                                                      @RequestParam(value = "cguAccepted", required = true) boolean cguAccepted,
                                                      @RequestParam(value = "cguAccepted", required = true) String pseudo) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return new ResponseEntity<>(
                        new CompteResponse(email, "Secret inconnu", Constantes.RETOUR_API_KO), HttpStatus.UNAUTHORIZED);
            }
            if(!cguAccepted) {
                return new ResponseEntity<>(
                        new CompteResponse(email, Utils.getMessage(Constantes.CGU_NON_ACCEPTE_KEY, langue), Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (compteService.compteExiste(email)) {
                return new ResponseEntity<>(new CompteResponse(email,
                        Utils.getMessage(Constantes.COMPTE_EXISTE_KEY, langue), Constantes.RETOUR_API_KO),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (compteService.pseudoExiste(pseudo)) {
                return new ResponseEntity<>(new CompteResponse(email,
                        Utils.getMessage(Constantes.PSEUDO_EXISTE_KEY, langue), Constantes.RETOUR_API_KO),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            compteService.creationCompte(email, password, cguAccepted, pseudo);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new CompteResponse(email,
                    Utils.getMessage(Constantes.COMPTE_ERROR_KEY, langue) + " : " + e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                new CompteResponse(email, "Le compte s'est bien créé", Constantes.RETOUR_API_OK), HttpStatus.OK);
    }

    /**
     * Mettre a jour le mot de passe d'un compte
     *
     * @param cossy       : cossy du compte
     * @param oldPassword : ancien mot de passe
     * @param newPassword : nouveau mot de passe
     * @param secret      : secret de l'application appelante afin de s'authentifier
     * @return ResponseEntity<CompteResponse> contenant le cossy, le code retour (0
     * si ok) et le messegae retour
     */
    @PostMapping("/update-password")
    public ResponseEntity<CompteResponse> updatePassword(@RequestParam(value = "cossy", required = true) String cossy,
                                                         @RequestParam(value = "oldPassword", required = true) String oldPassword,
                                                         @RequestParam(value = "newPassword", required = true) String newPassword,
                                                         @RequestHeader(value = "secret", required = true) String secret,
                                                         @RequestHeader(value = "secret", defaultValue = "1") Integer langue) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return new ResponseEntity<>(
                        new CompteResponse(cossy, "Secret inconnu", Constantes.RETOUR_API_KO), HttpStatus.UNAUTHORIZED);
            }
            boolean isUpdate = compteService.updatePassword(cossy, oldPassword, newPassword);
            if (!isUpdate) {
                return new ResponseEntity<>(new CompteResponse(cossy,
                        Utils.getMessage(Constantes.PASSWORD_DIFFERENT_KEY, langue),
                        Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new CompteResponse(cossy,
                    "Une erreur est survenue, le mot de passe n'a pas etait mit a jour : " + e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                new CompteResponse(cossy, "Le mot de passe a bien été mit à jour", Constantes.RETOUR_API_OK),
                HttpStatus.OK);
    }

    /**
     * API permetant de supprimer un compte
     *
     * @param cossy   : cossy du joueur
     * @param secret: secret de l'application appelante afin de s'authentifier
     * @return ResponseEntity<CompteResponse> contenant le cossy, le code retour (0
     * si ok) et le messegae retour
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<CompteResponse> supprimerCompte(@RequestParam(value = "cossy", required = true) String cossy,
                                                          @RequestHeader(value = "secret", required = true) String secret) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return new ResponseEntity<>(
                        new CompteResponse(cossy, "Secret inconnu", Constantes.RETOUR_API_KO), HttpStatus.UNAUTHORIZED);
            }
            if (!compteService.supprimerCompte(cossy)) {
                return new ResponseEntity<>(
                        new CompteResponse(cossy, "Le compte n'existe pas.", Constantes.RETOUR_API_KO),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new CompteResponse(cossy,
                    "Une erreur est survenue, le compte n'a pas été supprimé : " + e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                new CompteResponse(cossy, "Le compte a bien été supprimé", Constantes.RETOUR_API_OK), HttpStatus.OK);
    }

    @GetMapping("/get-token-of-account")
    public ResponseEntity<ConnexionResponse> getTokenConnexion(@RequestParam(value = "cossy", required = true) String cossy,
                                                               @RequestParam(value = "password", required = true) String password,
                                                               @RequestHeader(value = "secret", required = true) String secret) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return new ResponseEntity<>(
                        new ConnexionResponse(null, "Secret inconnu", Constantes.RETOUR_API_KO), HttpStatus.UNAUTHORIZED);
            }
            if (compteService.connexion(cossy, password) != null) {
                TokenDto token = compteService.getTokenDtoByEmail(cossy);
                return new ResponseEntity<>(new ConnexionResponse(token.getToken(), token.getTokenExpireDate(), token.getCossy(), "Token correctement récupéré", Constantes.RETOUR_API_OK), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ConnexionResponse(null, "Cossy ou mot de passe incorect", Constantes.RETOUR_API_KO), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ConnexionResponse(null,
                    "Une erreur est survenue : " + e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-account-of-token")
    public ResponseEntity<ConnexionResponse> getCossyOfToken(@RequestParam(value = "token", required = true) String token,
                                                             @RequestHeader(value = "secret", required = true) String secret) {
        try {
            if (!secretService.verifierSecret(secret)) {
                return new ResponseEntity<>(
                        new ConnexionResponse(null, "Secret inconnu", Constantes.RETOUR_API_KO), HttpStatus.UNAUTHORIZED);
            }
            TokenDto tokenOut = compteService.getTokenDtoByToken(token);
            return new ResponseEntity<>(new ConnexionResponse(tokenOut.getToken(), tokenOut.getTokenExpireDate(), tokenOut.getCossy(), "Infos correctement récupéré", Constantes.RETOUR_API_OK), HttpStatus.OK);
        } catch (TokenExpiredException e) {
            return new ResponseEntity<>(new ConnexionResponse(null,
                    e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.FORBIDDEN);
        } catch (CompteNotFoundException e) {
            return new ResponseEntity<>(new ConnexionResponse(null,
                    e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            return new ResponseEntity<>(new ConnexionResponse(null,
                    "Une erreur est survenue : " + e.getMessage(),
                    Constantes.RETOUR_API_KO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("userId") String email, @RequestParam("key") String key) {
        boolean activated = compteService.activateUser(email, key);
        if (activated) {
            return "Votre compte a été activé avec succès.";
        } else {
            return "Le lien d'activation est invalide ou a expiré.";
        }
    }

}
