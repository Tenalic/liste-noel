package sc.liste.noel.liste_noel.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sc.liste.noel.liste_noel.Utile.Utils;
import sc.liste.noel.liste_noel.constante.CheminConstante;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;
import sc.liste.noel.liste_noel.constante.NomPageConstante;
import sc.liste.noel.liste_noel.dto.response.CompteResponse;
import sc.liste.noel.liste_noel.ressource.CompteRessource;
import sc.liste.noel.liste_noel.service.SecretServiceInterface;

import java.util.Optional;
import static sc.liste.noel.liste_noel.constante.CheminConstante.REDIRECT;
import static sc.liste.noel.liste_noel.constante.CheminConstante.CONSULTER_LISTE;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.UPDATE_PASSWORD;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.CGU;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.POLITIQUE_CONFIDENTIALITE;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.POLITIQUE_SECURITE;
import static sc.liste.noel.liste_noel.constante.NomPageConstante.INSCRIPTION;


@Controller
public class InscriptionController {

    @Autowired
    private CompteRessource compteRessource;

    @Autowired
    private SecretServiceInterface secretService;

    @GetMapping("/inscription")
    public String inscriptionGet(Model model, HttpSession session) {
        if (session.getAttribute(ConstantesSession.EMAIL) != null) {
            return REDIRECT + CONSULTER_LISTE;
        }
        Utils.setupModel(session, model);
        return INSCRIPTION;
    }

    @PostMapping("/inscription")
    public String inscriptionPost(@RequestParam(value = "email", required = true) String email,
                                  @RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "confirmationNewPassword", required = true) String confirmationNewPassword,
                                  @RequestParam(value = "cgu", defaultValue = "false") boolean cgu,
                                  @RequestParam(value = "pseudo", required = true) String pseudo,
                                  Model model, HttpSession session) {


        if (!cgu) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CGU_NON_ACCEPTE_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.INSCRIPTION;
        }

        if(Utils.isInvalidEmail(email)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.EMAIL_NON_ACCEPTE_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.INSCRIPTION;
        }

        if (!confirmationNewPassword.equals(password)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.MDP_NOT_EQUALS_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.INSCRIPTION;
        }

        CompteResponse compteResponse = compteRessource.creerCompte(email, password, secretService.getMySecret(), Constantes.CODE_FRANCAIS, cgu, pseudo)
                .getBody();

        if (compteResponse.getCodeRetour() != 0) {
            Utils.setSessionErrorMessage(session, compteResponse.getMessageRetour());
            return REDIRECT + CheminConstante.INSCRIPTION;
        }

        session.setAttribute(ConstantesSession.EMAIL, email);

        return REDIRECT + CONSULTER_LISTE;
    }

    @GetMapping("/modifier-password")
    public String modifierPasswordGet(String password, Model model, HttpSession session) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);


        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, langue));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        Utils.setupModel(session, model);

        return UPDATE_PASSWORD;
    }

    @PostMapping("/modifier-password")
    public String modifierPasswordPost(@RequestParam(value = "oldPassword", required = true) String oldPassword,
                                       @RequestParam(value = "newPassword", required = true) String newPassword,
                                       @RequestParam(value = "confirmationNewPassword", required = true) String confirmationNewPassword,
                                       String password, Model model, HttpSession session) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);


        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, langue));
            return REDIRECT + CheminConstante.CONNEXION;
        }

        if (!confirmationNewPassword.equals(newPassword)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.NEW_MDP_NOT_EQUALS_KEY, langue));
            return REDIRECT + CheminConstante.MODIFIER_PASSWORD;
        }

        CompteResponse compteResponse = compteRessource
                .updatePassword(email, oldPassword, newPassword, secretService.getMySecret(), langue).getBody();

        if (compteResponse.getCodeRetour() == Constantes.RETOUR_API_OK) {
            model.addAttribute(ConstantesSession.SUCCES, compteResponse.getMessageRetour());
        } else {
            model.addAttribute(ConstantesSession.ERREUR, compteResponse.getMessageRetour());
        }

        Utils.setupModel(session, model);

        return UPDATE_PASSWORD;
    }

    @GetMapping("/cgu")
    public String cguGet(HttpSession session, Model model) {
        Utils.setupModel(session, model);
        return CGU;
    }

    @GetMapping("/politique-confidentialite")
    public String politiqueConfidentialiteGet(HttpSession session, Model model) {
        Utils.setupModel(session, model);
        return POLITIQUE_CONFIDENTIALITE;
    }

    @GetMapping("/politique-securite")
    public String politiqueSecuriteGet(HttpSession session, Model model) {
        Utils.setupModel(session, model);
        return POLITIQUE_SECURITE;
    }
}
