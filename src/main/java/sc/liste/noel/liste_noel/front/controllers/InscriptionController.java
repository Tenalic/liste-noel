package sc.liste.noel.liste_noel.front.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sc.liste.noel.liste_noel.front.Utile.Utils;
import sc.liste.noel.liste_noel.front.constante.Constantes;
import sc.liste.noel.liste_noel.front.constante.ConstantesSession;
import sc.liste.noel.liste_noel.back.dto.CompteResponse;
import sc.liste.noel.liste_noel.back.ressource.CompteRessource;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;
import sc.liste.noel.liste_noel.common.service.MessageService;

import java.util.Locale;

import static sc.liste.noel.liste_noel.front.constante.CheminConstante.*;
import static sc.liste.noel.liste_noel.front.constante.Constantes.*;
import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.ERREUR;
import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.SUCCES;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.UPDATE_PASSWORD;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.CGU;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.POLITIQUE_CONFIDENTIALITE;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.POLITIQUE_SECURITE;
import static sc.liste.noel.liste_noel.front.constante.NomPageConstante.INSCRIPTION;


@Controller
public class InscriptionController {

    @Autowired
    private CompteRessource compteRessource;

    @Autowired
    private SecretServiceInterface secretService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/inscription")
    public String inscriptionGet(HttpSession session) {
        if (session.getAttribute(ConstantesSession.EMAIL) != null) {
            return REDIRECT + CONSULTER_LISTE;
        }
        return INSCRIPTION;
    }

    @PostMapping("/inscription")
    public String inscriptionPost(@RequestParam(value = "email") String email,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "confirmationNewPassword") String confirmationNewPassword,
                                  @RequestParam(value = "cgu", defaultValue = "false") boolean cgu,
                                  @RequestParam(value = "pseudo") String pseudo,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {

        Locale locale = request.getLocale();
        if (!cgu) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CGU_NON_ACCEPTE_KEY, locale));
            return REDIRECT + INSCRIPTION;
        }

        if (Utils.isInvalidEmail(email)) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(EMAIL_NON_ACCEPTE_KEY, locale));
            return REDIRECT + INSCRIPTION;
        }

        if (!confirmationNewPassword.equals(password)) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(MDP_NOT_EQUALS_KEY, locale));
            return REDIRECT + INSCRIPTION;
        }

        CompteResponse compteResponse = compteRessource.creerCompte(email, password, secretService.getMySecret(), pseudo, cgu, locale)
                .getBody();

        if (compteResponse.getCodeRetour() != 0) {
            redirectAttributes.addFlashAttribute(ERREUR, compteResponse.getMessageRetour());
            return REDIRECT + INSCRIPTION;
        }

        session.setAttribute(ConstantesSession.EMAIL, email);

        return REDIRECT + CONSULTER_LISTE;
    }

    @GetMapping("/modifier-password")
    public String modifierPasswordGet(HttpSession session
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        return UPDATE_PASSWORD;
    }

    @PostMapping("/modifier-password")
    public String modifierPasswordPost(@RequestParam(value = "oldPassword") String oldPassword
            , @RequestParam(value = "newPassword") String newPassword
            , @RequestParam(value = "confirmationNewPassword") String confirmationNewPassword
            , Model model
            , HttpSession session
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        if (!confirmationNewPassword.equals(newPassword)) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(NEW_MDP_NOT_EQUALS_KEY, locale));
            return REDIRECT + MODIFIER_PASSWORD;
        }

        CompteResponse compteResponse = compteRessource
                .updatePassword(email, oldPassword, newPassword, secretService.getMySecret(), locale).getBody();

        if (compteResponse.getCodeRetour() == Constantes.RETOUR_API_OK) {
            redirectAttributes.addFlashAttribute(SUCCES, compteResponse.getMessageRetour());
        } else {
            redirectAttributes.addFlashAttribute(ERREUR, compteResponse.getMessageRetour());
        }

        return UPDATE_PASSWORD;
    }

    @GetMapping("/cgu")
    public String cguGet(HttpSession session, Model model) {
        return CGU;
    }

    @GetMapping("/politique-confidentialite")
    public String politiqueConfidentialiteGet(HttpSession session, Model model) {
        return POLITIQUE_CONFIDENTIALITE;
    }

    @GetMapping("/politique-securite")
    public String politiqueSecuriteGet(HttpSession session, Model model) {
        return POLITIQUE_SECURITE;
    }
}
