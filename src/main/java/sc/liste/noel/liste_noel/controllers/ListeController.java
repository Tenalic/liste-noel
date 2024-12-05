package sc.liste.noel.liste_noel.controllers;

import jakarta.servlet.http.HttpSession;
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
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

import java.util.List;


@Controller
public class ListeController {

    private static final Logger LOGGER = LogManager.getLogger(ListeController.class);

    @Autowired
    private ListeServiceInterface listeServiceInterface;

    @GetMapping("/liste")
    public String getListe(Model model, HttpSession session) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        if(email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return "redirect:connexion";
        }
        Utils.getSessionErrorMessage(session, model);

        List<ListeDto> listDeListeDto = listeServiceInterface.getListeOfEmail(email);

        model.addAttribute(ConstantesSession.LISTES, listDeListeDto);

        return "liste" ;
    }

    @PostMapping("/creer-liste")
    public String creerListe(Model model, HttpSession session, @RequestParam(value = "nomListe", required = true) String nomListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if(email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return "redirect:connexion";
        }
        Utils.getSessionErrorMessage(session, model);
        try {
            listeServiceInterface.creerListe(email, nomListe);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return "liste" ;
    }

    @PostMapping("/selectionner-liste")
    public String selectionnerListe(Model model, HttpSession session,
                                    @RequestParam(value = "idListe", required = true) String idListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if(email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return "redirect:connexion";
        }
        Utils.getSessionErrorMessage(session, model);
        try {
            ListeDto listeDto = listeServiceInterface.getListeById(idListe);
            session.setAttribute(ConstantesSession.LISTES, listeDto);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
            return "redirect:liste" ;
        }
        return "consulterListe" ;
    }

    @PostMapping("/ajouter-objet")
    public String ajouterObjet(Model model, HttpSession session,
                               @RequestParam(value = "titre", required = true) String titre,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "idListe", required = true) String idListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if(email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return "redirect:connexion";
        }
        Utils.getSessionErrorMessage(session, model);
        try {
            listeServiceInterface.ajouterObjet(titre, url,description,idListe, email);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return "liste" ;
    }
}
