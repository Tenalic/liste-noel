package sc.liste.noel.liste_noel.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sc.liste.noel.liste_noel.Utile.Utils;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;
import sc.liste.noel.liste_noel.dto.response.CompteResponse;
import sc.liste.noel.liste_noel.ressource.CompteRessource;
import sc.liste.noel.liste_noel.service.SecretServiceInterface;

import java.util.Optional;

@Controller
public class InscriptionController {

    @Autowired
    private CompteRessource compteRessource;

    @Autowired
    private SecretServiceInterface secretService;

    @GetMapping("/inscription")
    public String inscriptionGet(Model model, HttpSession session) {
        if (session.getAttribute(ConstantesSession.EMAIL) != null) {
            return "redirect:tournois";
        }
        Utils.getSessionErrorMessage(session, model);

        Integer langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);
        return (langue == Constantes.CODE_FRANCAIS ? "inscription" : "inscriptionEN");
    }

    @PostMapping("/inscription")
    public String inscriptionPost(@RequestParam(value = "email", required = true) String email,
                                  @RequestParam(value = "password", required = true) String password,
                                  @RequestParam(value = "confirmationNewPassword", required = true) String confirmationNewPassword,
                                  @RequestParam(value = "cgu", defaultValue = "false") boolean cgu,
                                  Model model, HttpSession session) {

        Integer langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);

        if (!cgu) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CGU_NON_ACCEPTE_KEY, langue));
            return "redirect:inscription";
        }

        if (!confirmationNewPassword.equals(password)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.MDP_NOT_EQUALS_KEY, langue));
            return "redirect:inscription";
        }

        CompteResponse compteResponse = compteRessource.creerCompte(email, password, secretService.getMySecret(), langue, cgu)
                .getBody();
        assert compteResponse != null;
        if (compteResponse.getCodeRetour() != 0) {
            Utils.setSessionErrorMessage(session, compteResponse.getMessageRetour());
            return "redirect:inscription";
        }

        session.setAttribute(ConstantesSession.EMAIL, email);

        return "redirect:tournois";
    }

    @GetMapping("/modifier-password")
    public String modifierPasswordGet(String password, Model model, HttpSession session) {

        String cossy = (String) session.getAttribute(ConstantesSession.EMAIL);
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);


        if (cossy == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, langue));
            return "redirect:connexion";
        }
        Utils.getSessionErrorMessage(session, model);

        return (langue == Constantes.CODE_FRANCAIS ? "updatePassword" : "updatePasswordEN");
    }

    @PostMapping("/modifier-password")
    public String modifierPasswordPost(@RequestParam(value = "oldPassword", required = true) String oldPassword,
                                       @RequestParam(value = "newPassword", required = true) String newPassword,
                                       @RequestParam(value = "confirmationNewPassword", required = true) String confirmationNewPassword,
                                       String password, Model model, HttpSession session) {

        String cossy = (String) session.getAttribute(ConstantesSession.EMAIL);
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);


        if (cossy == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, langue));
            return "redirect:connexion";
        }

        if (!confirmationNewPassword.equals(newPassword)) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.NEW_MDP_NOT_EQUALS_KEY, langue));
            return "redirect:modifier-password";
        }

        CompteResponse compteResponse = compteRessource
                .updatePassword(cossy, oldPassword, newPassword, secretService.getMySecret(), langue).getBody();

        assert compteResponse != null;
        if (compteResponse.getCodeRetour() == Constantes.RETOUR_API_OK) {
            model.addAttribute(ConstantesSession.SUCCES, compteResponse.getMessageRetour());
        } else {
            model.addAttribute(ConstantesSession.ERREUR, compteResponse.getMessageRetour());
        }

        return (langue == Constantes.CODE_FRANCAIS ? "updatePassword" : "updatePasswordEN");
    }

    @GetMapping("/cgu")
    public String getCGU(HttpSession session) {
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);
        return (langue == Constantes.CODE_FRANCAIS ? "cgu" : "cguEN");
    }

    @GetMapping("/politique-confidentialite")
    public String getPolitiqueConfidentialite(HttpSession session) {
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);
        return (langue == Constantes.CODE_FRANCAIS ? "politiqueConfidentialite" : "politiqueConfidentialiteEN");
    }

    @GetMapping("/politique-securite")
    public String getPolitiqueSecurite(HttpSession session) {
        int langue = (Integer) Optional.ofNullable(session.getAttribute(ConstantesSession.LANGUE)).orElse(1);
        return (langue == Constantes.CODE_FRANCAIS ? "politiqueSecurite" : "politiqueSecuriteEN");
    }
}
