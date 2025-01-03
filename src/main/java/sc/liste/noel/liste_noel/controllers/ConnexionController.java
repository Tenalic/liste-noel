package sc.liste.noel.liste_noel.controllers;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sc.liste.noel.liste_noel.Utile.Utils;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;
import sc.liste.noel.liste_noel.dto.CompteDto;
import sc.liste.noel.liste_noel.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.service.impl.MailService;

import java.util.Optional;

import static sc.liste.noel.liste_noel.constante.CheminConstante.REDIRECT;
import static sc.liste.noel.liste_noel.constante.CheminConstante.CONSULTER_LISTE;
import static sc.liste.noel.liste_noel.constante.CheminConstante.INSCRIPTION;
import static sc.liste.noel.liste_noel.constante.CheminConstante.LISTE;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.WELCOME;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.CONNEXION;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.CONTACT;

@Controller
public class ConnexionController {

    private static final Logger LOGGER = LogManager.getLogger(ConnexionController.class);

    @Autowired
    private MailService mailService;
    @Autowired
    private CompteServiceInterface compteService;

    @GetMapping(value = {"", "/", "welcome", "ma-liste-de-cadeau"})
    public String welcomeGet(Model model, HttpSession session) {
        Utils.setupModel(session, model);
        return WELCOME;
    }

    @GetMapping(value = {"connexion"})
    public String connexionGet(Model model, HttpSession session) {

        // Si utilisateur déjà connecté
        if (session.getAttribute(ConstantesSession.EMAIL) != null) {
            return REDIRECT + LISTE;
        }

        Utils.setupModel(session, model);

        return CONNEXION;
    }

    @PostMapping("/connexion")
    public String connexionPost(@RequestParam(value = "email", required = true) String email, @RequestParam(value = "password", required = true) String password, Model model, HttpSession session) {
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);
        try {
            CompteDto compteDto = compteService.connexion(email, password);
            if (compteDto != null) {
                session.setMaxInactiveInterval(14400);
                session.setAttribute(ConstantesSession.EMAIL, email);
                session.setAttribute(ConstantesSession.PSEUDO, compteDto.getPseudo());
                Long idShared = (Long) session.getAttribute(Constantes.SHARED_LISTE);
                if (idShared != null) {
                    return REDIRECT + CONSULTER_LISTE;
                } else {
                    return REDIRECT + LISTE;
                }
            } else {
                Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_FAIL_KEY, langue));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, langue) + " : " + e.getMessage());
        }
        return REDIRECT + CONNEXION;
    }

    @GetMapping("/deconnexion")
    public String deconnexionGet(Model model, HttpSession session) {
        try {
            String email = (String) session.getAttribute(ConstantesSession.EMAIL);
            if (email != null) {
                compteService.deconexion(email);
            }
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        session.setAttribute(ConstantesSession.EMAIL, null);
        session.removeAttribute(ConstantesSession.ID_LISTE);
        return REDIRECT + CONNEXION;
    }

    @GetMapping(value = {"contact"})
    public String contactGet(Model model, HttpSession session) {
        Utils.setupModel(session, model);
        return CONTACT;
    }

    @PostMapping(value = {"mot-de-passe-oublie"})
    public String motDePasseOubliePost(Model model, HttpSession session, @RequestParam(value = "email", required = true) String email) {

        Integer langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);

        if (Utils.isInvalidEmail(email)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.EMAIL_NON_ACCEPTE_KEY, langue));
            return REDIRECT + INSCRIPTION;
        }

        try {
            compteService.genererMotDePasseEtEnvoyer(email);
            Utils.setSessionInfoMessage(session, "Si un compte avec l'email " + email + " existe, vous devriez avoir reçu un nouveau mot de passe.");
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CONNEXION;
    }


}
