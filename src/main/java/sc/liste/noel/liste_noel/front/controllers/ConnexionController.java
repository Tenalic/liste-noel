package sc.liste.noel.liste_noel.front.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sc.liste.noel.liste_noel.back.service.CompteServiceInterface;
import sc.liste.noel.liste_noel.common.service.MessageService;
import sc.liste.noel.liste_noel.common.utils.Utils;
import sc.liste.noel.liste_noel.common.dto.CompteDto;
import sc.liste.noel.liste_noel.back.service.impl.MailService;

import java.util.Locale;

import static sc.liste.noel.liste_noel.front.constante.CheminConstante.*;
import static sc.liste.noel.liste_noel.front.constante.CheminConstante.LISTE;
import static sc.liste.noel.liste_noel.front.constante.Constantes.*;
import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.*;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.*;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.CONNEXION;

@Controller
public class ConnexionController {

    private static final Logger LOGGER = LogManager.getLogger(ConnexionController.class);

    @Autowired
    private MailService mailService;
    @Autowired
    private CompteServiceInterface compteService;
    @Autowired
    private MessageService messageService;

    @GetMapping(value = {"", "/", "welcome", "ma-liste-de-cadeau"})
    public String welcomeGet() {
        return WELCOME;
    }

    @GetMapping(value = {"connexion"})
    public String connexionGet(HttpSession session) {

        // Si utilisateur déjà connecté
        if (session.getAttribute(EMAIL) != null) {
            return REDIRECT + LISTE;
        }

        return CONNEXION;
    }

    @PostMapping("/connexion")
    public String connexionPost(@RequestParam(value = "email") String email
            , @RequestParam(value = "password") String password
            , HttpSession session
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {

        Locale locale = request.getLocale();

        // Validation de l'email
        if (Utils.isInvalidEmail(email)) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(EMAIL_NON_ACCEPTE_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        try {
            CompteDto compteDto = compteService.connexion(email, password);
            if (compteDto != null) {
                session.setMaxInactiveInterval(14400);
                session.setAttribute(EMAIL, email);
                session.setAttribute(PSEUDO, compteDto.getPseudo());
                session.setAttribute(CONNECTED, true);

                Long idShared = (Long) session.getAttribute(SHARED_LISTE);

                // Si l'utilisateur s'est connecté pour consulter une liste qu'on lui a partagée
                // On le redirige vers la liste
                if (idShared != null) {
                    return REDIRECT + CONSULTER_LISTE;
                } else {
                    return REDIRECT + LISTE;
                }
            } else {
                redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_FAIL_KEY, locale));
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CONNEXION;
    }

    @GetMapping("/deconnexion")
    public String deconnexionGet(HttpSession session) {
        try {
            String email = (String) session.getAttribute(EMAIL);
            if (email != null) {
                compteService.deconexion(email);
            }
        } catch (Exception e) {
            LOGGER.warn("Erreur lors de la déconnexion", e);
        }
        // Invalidation complète de la session
        session.invalidate();
        return REDIRECT + CONNEXION;
    }

    @GetMapping(value = {"contact"})
    public String contactGet() {
        return CONTACT;
    }

    @PostMapping(value = {"mot-de-passe-oublie"})
    public String motDePasseOubliePost(HttpServletRequest request
            , RedirectAttributes redirectAttributes
            , @RequestParam(value = "email") String email) {

        Locale locale = request.getLocale();

        if (Utils.isInvalidEmail(email)) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(EMAIL_NON_ACCEPTE_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        try {
            compteService.genererMotDePasseEtEnvoyer(email);
            redirectAttributes.addFlashAttribute(INFO, messageService.getMessage(MOT_DE_PASSE_OUBLIE_P1_KEY, locale) + email + messageService.getMessage(MOT_DE_PASSE_OUBLIE_P2_KEY, locale));
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CONNEXION;
    }


}
