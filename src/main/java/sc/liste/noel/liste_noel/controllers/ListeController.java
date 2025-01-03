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
import sc.liste.noel.liste_noel.constante.CheminConstante;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;
import sc.liste.noel.liste_noel.constante.NomPageConstante;
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.dto.ObjetDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static sc.liste.noel.liste_noel.constante.CheminConstante.REDIRECT;


@Controller
public class ListeController {

    private static final Logger LOGGER = LogManager.getLogger(ListeController.class);

    @Autowired
    private ListeServiceInterface listeServiceInterface;

    @GetMapping("/liste")
    public String listeGet(Model model, HttpSession session) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        List<ListeDto> listDeListeDto = listeServiceInterface.getListesOfEmail(email);
        model.addAttribute(ConstantesSession.LISTES, listDeListeDto);
        List<ListeDto> listDeListeDtoFavoris = listeServiceInterface.getListeFavorisOfEmail(email);
        model.addAttribute(ConstantesSession.LISTES_FAVORIS, listDeListeDtoFavoris);
        Utils.setupModel(session, model);

        return NomPageConstante.LISTE;
    }


    @GetMapping("/partage")
    public String partageGet(Model model, HttpSession session, @RequestParam(value = "id", required = true) String idListe) {

        session.setAttribute(Constantes.SHARED_LISTE, Long.valueOf(idListe));

        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/creer-liste")
    public String creerListePost(Model model, HttpSession session, @RequestParam(value = "nomListe", required = true) String nomListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        Utils.setupModel(session, model);
        try {
            listeServiceInterface.creerListe(email, nomListe);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.LISTE;
    }

    @GetMapping("/consulter-liste")
    public String consulterListeGet(Model model, HttpSession session, @RequestParam(value = "triPriorite", required = false, defaultValue = "asc") String triPriorite) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        Long idShared = (Long) session.getAttribute(Constantes.SHARED_LISTE);
        if (idShared != null) {
            if (email != null) {
                session.removeAttribute(Constantes.SHARED_LISTE);
            }
            session.setAttribute(ConstantesSession.ID_LISTE, idShared);
        }

        Long idListe = (Long) session.getAttribute(ConstantesSession.ID_LISTE);

        if (idListe == null) {
            return REDIRECT + CheminConstante.LISTE;
        }

        ListeDto listeDto;

        try {
            listeDto = listeServiceInterface.getListeById(idListe);

            // Tri basé sur la priorité
            if ("asc".equalsIgnoreCase(triPriorite)) {
                listeDto.getListeObjet().sort(Comparator.comparing(ObjetDto::getValuePriorite));
            } else if ("desc".equalsIgnoreCase(triPriorite)) {
                listeDto.getListeObjet().sort(Comparator.comparing(ObjetDto::getValuePriorite).reversed());
            }

            model.addAttribute("triPriorite", triPriorite);
            model.addAttribute(ConstantesSession.LISTE, listeDto);
            model.addAttribute(ConstantesSession.EMAIL, email);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
            return REDIRECT + CheminConstante.LISTE;
        }

        Utils.setupModel(session, model);

        if (email != null && email.equals(Optional.of(listeDto).map(ListeDto::getProprietaire).orElse(null))) {
            return NomPageConstante.CONSULTER_LISTE_PROPRIETAIRE;
        } else {
            // ajouter valeur est dans favoris
            boolean estDansFavoris = false;
            if (email != null) {
                estDansFavoris = listeServiceInterface.checkifListeInFavoris(idListe, email);
            }
            model.addAttribute(ConstantesSession.IS_FAVORI, estDansFavoris);
            return NomPageConstante.CONSULTER_LISTE_PARTICIPANT;
        }
    }

    @PostMapping("/selectionner-liste")
    public String selectionnerListePost(Model model, HttpSession session,
                                        @RequestParam(value = "idListe", required = false) String idListe,
                                        @RequestParam(value = "listeFavoris", required = false) String idListeFavoris) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        Utils.setupModel(session, model);
        ListeDto listeDto;
        try {
            String id = idListe == null ? idListeFavoris : idListe;
            listeDto = listeServiceInterface.getListeById(Long.valueOf(id));
            session.setAttribute(ConstantesSession.LISTE, listeDto);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
            return REDIRECT + CheminConstante.LISTE;
        }

        if (listeDto == null) {
            return REDIRECT + CheminConstante.LISTE;
        }

        session.setAttribute(ConstantesSession.ID_LISTE, listeDto.getIdListe());

        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/ajouter-objet")
    public String ajouterObjet(Model model, HttpSession session,
                               @RequestParam(value = "titre", required = true) String titre,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "priorite", required = false) String priorite,
                               @RequestParam(value = "idListe", required = true) String idListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        Utils.setupModel(session, model);
        try {
            listeServiceInterface.ajouterObjetDansUneListe(titre, url, description, idListe, email, Integer.parseInt(priorite));
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/prendre")
    public String prendreObjet(Model model, HttpSession session,
                               @RequestParam(value = "idListe", required = true) String idListe,
                               @RequestParam(value = "idObjet", required = true) String idObjet) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        String pseudo = (String) session.getAttribute(ConstantesSession.PSEUDO);
        Utils.setupModel(session, model);
        try {
            listeServiceInterface.prendreUnObjet(idListe, idObjet, email, pseudo);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/ne-plus-prendre")
    public String nePlusPrendreUnObjet(Model model, HttpSession session,
                                       @RequestParam(value = "idObjet", required = true) String idObjet) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        Utils.setupModel(session, model);
        try {
            listeServiceInterface.nePlusPrendreUnObjet(idObjet);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/ajouter-favori")
    public String ajouterFavoris(Model model, HttpSession session,
                                 @RequestParam(value = "idListeFavoris", required = true) String idListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        try {
            listeServiceInterface.ajouterFavori(Long.valueOf(idListe), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/supprimer-favori")
    public String supprimerFavori(Model model, HttpSession session,
                                  @RequestParam(value = "idListeFavoris", required = true) String idListe) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }
        try {
            listeServiceInterface.supprimerFavori(Long.valueOf(idListe), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/supprimer-objet")
    public String supprimerObjet(Model model, HttpSession session,
                                 @RequestParam(value = "idObjet", required = true) String idObjet) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }

        try {
            listeServiceInterface.supprimerObjet(Long.valueOf(idObjet), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/modifier-objet")
    public String modifierObjet(Model model, HttpSession session,
                                @RequestParam(value = "idObjet", required = true) String idObjet,
                                @RequestParam(value = "titreUpdate", required = true) String titreUpdate,
                                @RequestParam(value = "descriptionUpdate", required = false) String descriptionUpdate,
                                @RequestParam(value = "prioriteUpdate", required = false) String prioriteUpdate,
                                @RequestParam(value = "urlUpdate", required = false) String urlUpdate) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        if (email == null) {
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.CONNEXION_KEY, Constantes.CODE_FRANCAIS));
            return REDIRECT + CheminConstante.CONNEXION;
        }

        try {
            listeServiceInterface.modifierObjet(Long.valueOf(idObjet), titreUpdate, descriptionUpdate, urlUpdate, Integer.parseInt(prioriteUpdate));
        } catch (Exception e) {
            LOGGER.error("", e);
            Utils.setSessionErrorMessage(session, Utils.getMessage(Constantes.ERREUR_GENERIQUE_KAY, Constantes.CODE_FRANCAIS) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }


}
