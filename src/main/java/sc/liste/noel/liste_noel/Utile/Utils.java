package sc.liste.noel.liste_noel.Utile;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;

import java.util.List;

public class Utils {

    public static boolean aMessageErreur(String messageErreur) {
        if (messageErreur != null && !"".equals(messageErreur)) {
            return true;
        }
        return false;
    }

    public static void setSessionErrorMessage(HttpSession session, String messageErreur) {
        session.setAttribute(ConstantesSession.ERREUR, messageErreur);
    }

    public static void getSessionErrorMessage(HttpSession session, Model model) {
        String messageErreur = (String) session.getAttribute(ConstantesSession.ERREUR);
        if (aMessageErreur(messageErreur)) {
            session.removeAttribute(ConstantesSession.ERREUR);
            model.addAttribute(ConstantesSession.ERREUR, messageErreur);
        }
    }

    public static String getMessage(String keyMessage, int langue) {
        return switch (keyMessage) {
            case Constantes.CGU_NON_ACCEPTE_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.CGU_NON_ACCEPTE_EN : Constantes.CGU_NON_ACCEPTE_FR);
            case Constantes.CONNEXION_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.CONNEXION_EN : Constantes.CONNEXION_FR);
            case Constantes.MDP_NOT_EQUALS_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.MDP_NOT_EQUALS_EN : Constantes.MDP_NOT_EQUALS_FR);
            case Constantes.NEW_MDP_NOT_EQUALS_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.NEW_MDP_NOT_EQUALS_EN : Constantes.NEW_MDP_NOT_EQUALS_FR);
            case Constantes.CONNEXION_FAIL_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.CONNEXION_FAIL_EN : Constantes.CONNEXION_FAIL_FR);
            case Constantes.ERREUR_GENERIQUE_KAY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ERREUR_GENERIQUE_EN : Constantes.ERREUR_GENERIQUE_FR);
            case Constantes.ERREUR_RESULTAT_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ERREUR_RESULTAT_EN : Constantes.ERREUR_RESULTAT_FR);
            case Constantes.RESULTAT_EMPRTY_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.RESULTAT_EMPRTY_EN : Constantes.RESULTAT_EMPRTY_FR);
            case Constantes.ERREUR_RECUP_INFOS_TOURNOI_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ERREUR_RECUP_INFOS_TOURNOI_EN : Constantes.ERREUR_RECUP_INFOS_TOURNOI_FR);
            case Constantes.DROP_LOCK_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.DROP_LOCK_EN : Constantes.DROP_LOCK_FR);
            case Constantes.ERROR_DROP_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ERROR_DROP_EN : Constantes.ERROR_DROP_FR);
            case Constantes.ERROR_TOURNOIS_INSCRIT_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ERROR_TOURNOIS_INSCRIT_EN : Constantes.ERROR_TOURNOIS_INSCRIT_FR);
            case Constantes.ID_EMPTY_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.ID_EMPTY_EN : Constantes.ID_EMPTY_FR);
            case Constantes.COSSY_LETTRE_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.COSSY_LETTRE_EN : Constantes.COSSY_LETTRE_FR);
            case Constantes.COSSY_TAILLE_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.COSSY_TAILLE_EN : Constantes.COSSY_TAILLE_FR);
            case Constantes.COMPTE_EXISTE_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.COMPTE_EXISTE_EN : Constantes.COMPTE_EXISTE_FR);
            case Constantes.COMPTE_ERROR_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.COMPTE_ERROR_EN : Constantes.COMPTE_ERROR_FR);
            case Constantes.PASSWORD_DIFFERENT_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.PASSWORD_DIFFERENT_EN : Constantes.PASSWORD_DIFFERENT_FR);
            default ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.MESSAGE_DEFAUT_EN : Constantes.MESSAGE_DEFAUT_FR);
        };
    }

    public static int transcoLangue(String langue) {
        return switch (langue) {
            case "Français" -> Constantes.CODE_FRANCAIS;
            case "English" -> Constantes.CODE_ANGLAIS;
            default -> Constantes.CODE_FRANCAIS;
        };
    }


}
