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
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

import java.util.List;


@Controller
public class ListeController {

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

        model.addAttribute(ConstantesSession.LISTE, listDeListeDto);

        return "liste" ;
    }

    @PostMapping("/creer-liste")
    public String creerListe(Model model, HttpSession session, @RequestParam(value = "nomListe", required = true) String nomListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Utils.getSessionErrorMessage(session, model);
        listeServiceInterface.creerListe(email, nomListe);
        return "liste" ;
    }
}
