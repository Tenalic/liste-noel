package sc.liste.noel.liste_noel.front.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sc.liste.noel.liste_noel.back.service.ListeServiceInterface;
import sc.liste.noel.liste_noel.common.dto.ListeDto;
import sc.liste.noel.liste_noel.common.dto.ObjetDto;
import sc.liste.noel.liste_noel.common.service.MessageService;
import sc.liste.noel.liste_noel.front.constante.CheminConstante;
import sc.liste.noel.liste_noel.front.constante.ConstantesSession;
import sc.liste.noel.liste_noel.front.constante.NomPageConstante;
import sc.liste.noel.liste_noel.front.dtos.GeneriqueResponse;
import sc.liste.noel.liste_noel.front.services.ListeService;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static sc.liste.noel.liste_noel.front.constante.CheminConstante.*;
import static sc.liste.noel.liste_noel.front.constante.Constantes.CONNEXION_KEY;
import static sc.liste.noel.liste_noel.front.constante.Constantes.ERREUR_GENERIQUE_KEY;
import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.ERREUR;
import static sc.liste.noel.liste_noel.front.constante.ConstantesSession.INFO;


@Controller
public class ListeController {

    private static final Logger LOGGER = LogManager.getLogger(ListeController.class);

    private final ListeServiceInterface listeServiceInterface;

    private final MessageService messageService;

    private final ListeService listeService;

    public ListeController(ListeServiceInterface listeServiceInterface, MessageService messageService, ListeService listeService) {
        this.listeServiceInterface = listeServiceInterface;
        this.messageService = messageService;
        this.listeService = listeService;
    }

    @GetMapping("/liste")
    public String listeGet(Model model, HttpSession session) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);

        List<ListeDto> listDeListeDto = listeServiceInterface.getListesOfEmail(email);
        model.addAttribute(ConstantesSession.LISTES, listDeListeDto);
        List<ListeDto> listDeListeDtoFavoris = listeServiceInterface.getListeFavorisOfEmail(email);
        model.addAttribute(ConstantesSession.LISTES_FAVORIS, listDeListeDtoFavoris);

        return NomPageConstante.LISTE;
    }


    @GetMapping("/partage")
    public String partageGet(HttpSession session,
                             @RequestParam(value = "id") String idListe) {

        session.setAttribute(SHARED_LISTE, Long.valueOf(idListe));

        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/creer-liste")
    public String creerListePost(HttpSession session
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request
            , @RequestParam(value = "nomListe") String nomListe) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        try {
            listeServiceInterface.creerListe(email, nomListe);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale));
        }
        return REDIRECT + CheminConstante.LISTE;
    }

    @GetMapping("/consulter-liste")
    public String consulterListeGet(HttpServletRequest request
            , RedirectAttributes redirectAttributes
            , Model model
            , HttpSession session
            , @RequestParam(value = "triPriorite", required = false, defaultValue = "asc") String triPriorite) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        Long idShared = (Long) session.getAttribute(SHARED_LISTE);
        if (idShared != null) {
            if (email != null) {
                session.removeAttribute(SHARED_LISTE);
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
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale));
            return REDIRECT + LISTE;
        }

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
    public String selectionnerListePost(HttpSession session,
                                        @RequestParam(value = "idListe", required = false) String idListe,
                                        @RequestParam(value = "listeFavoris", required = false) String idListeFavoris
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        ListeDto listeDto;
        try {
            String id = idListe == null ? idListeFavoris : idListe;
            listeDto = listeServiceInterface.getListeById(Long.valueOf(id));
            session.setAttribute(ConstantesSession.LISTE, listeDto);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale));
            return REDIRECT + LISTE;
        }

        if (listeDto == null) {
            return REDIRECT + LISTE;
        }

        session.setAttribute(ConstantesSession.ID_LISTE, listeDto.getIdListe());

        return REDIRECT + CONSULTER_LISTE;
    }

    @PostMapping("/ajouter-objet")
    public String ajouterObjet(HttpSession session,
                               @RequestParam(value = "titre") String titre,
                               @RequestParam(value = "url", required = false) String url,
                               @RequestParam(value = "description", required = false) String description,
                               @RequestParam(value = "priorite", required = false) String priorite,
                               @RequestParam(value = "idListe") String idListe,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        try {
            listeServiceInterface.ajouterObjetDansUneListe(titre, url, description, idListe, email, Integer.parseInt(priorite));
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/prendre")
    public String prendreObjet(HttpSession session,
                               @RequestParam(value = "idListe") String idListe,
                               @RequestParam(value = "idObjet") String idObjet,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        String pseudo = (String) session.getAttribute(ConstantesSession.PSEUDO);
        try {
            listeServiceInterface.prendreUnObjet(idListe, idObjet, email, pseudo);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/ne-plus-prendre")
    public String nePlusPrendreUnObjet(HttpSession session
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request
            , @RequestParam(value = "idObjet") String idObjet) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        try {
            listeServiceInterface.nePlusPrendreUnObjet(idObjet);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/ajouter-favori")
    public String ajouterFavoris(HttpSession session
            , @RequestParam(value = "idListeFavoris") String idListe
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();
        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        try {
            listeServiceInterface.ajouterFavori(Long.valueOf(idListe), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/supprimer-favori")
    public String supprimerFavori(HttpSession session,
                                  @RequestParam(value = "idListeFavoris") String idListe,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }
        try {
            listeServiceInterface.supprimerFavori(Long.valueOf(idListe), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/supprimer-objet")
    public String supprimerObjet(HttpSession session
            , @RequestParam(value = "idObjet") String idObjet
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {
        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        try {
            listeServiceInterface.supprimerObjet(Long.valueOf(idObjet), email);
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/modifier-objet")
    public String modifierObjet(HttpSession session,
                                @RequestParam(value = "idObjet") String idObjet,
                                @RequestParam(value = "titreUpdate") String titreUpdate,
                                @RequestParam(value = "descriptionUpdate", required = false) String descriptionUpdate,
                                @RequestParam(value = "prioriteUpdate", required = false) String prioriteUpdate,
                                @RequestParam(value = "urlUpdate", required = false) String urlUpdate,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        try {
            listeServiceInterface.modifierObjet(Long.valueOf(idObjet), titreUpdate, descriptionUpdate, urlUpdate, Integer.parseInt(prioriteUpdate));
        } catch (Exception e) {
            LOGGER.error("", e);
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(ERREUR_GENERIQUE_KEY, locale) + " : " + e.getMessage());
        }
        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }

    @PostMapping("/supprimer-liste")
    public String supprimerListe(HttpSession session,
                                 @RequestParam(value = "nomListe") String nomListe,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {

        String email = (String) session.getAttribute(ConstantesSession.EMAIL);
        Locale locale = request.getLocale();

        if (email == null) {
            redirectAttributes.addFlashAttribute(ERREUR, messageService.getMessage(CONNEXION_KEY, locale));
            return REDIRECT + CONNEXION;
        }

        GeneriqueResponse generiqueResponse = listeService.supprimerListe(email, nomListe, locale);
        redirectAttributes.addFlashAttribute(generiqueResponse.getCodeRetour() == 0 ? INFO : ERREUR, generiqueResponse.getMessageRetour());

        return REDIRECT + CheminConstante.CONSULTER_LISTE;
    }


}
