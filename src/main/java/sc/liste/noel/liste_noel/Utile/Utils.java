package sc.liste.noel.liste_noel.Utile;

import jakarta.servlet.http.HttpSession;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.ui.Model;
import sc.liste.noel.liste_noel.constante.Constantes;
import sc.liste.noel.liste_noel.constante.ConstantesSession;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static final String ERROR_CODE = "ERRONEOUS_SPECIAL_CHARS";

    public static boolean aMessage(String message) {
        if (message != null && !"".equals(message)) {
            return true;
        }
        return false;
    }

    public static void setSessionErrorMessage(HttpSession session, String messageErreur) {
        session.setAttribute(ConstantesSession.ERREUR, messageErreur);
    }

    public static void setSessionInfoMessage(HttpSession session, String messageInfo) {
        session.setAttribute(ConstantesSession.INFO, messageInfo);
    }

    public static void setupModel(HttpSession session, Model model) {
        String messageErreur = (String) session.getAttribute(ConstantesSession.ERREUR);
        if (aMessage(messageErreur)) {
            session.removeAttribute(ConstantesSession.ERREUR);
            model.addAttribute(ConstantesSession.ERREUR, messageErreur);
        }
        String info = (String) session.getAttribute(ConstantesSession.INFO);
        if (aMessage(info)) {
            session.removeAttribute(ConstantesSession.INFO);
            model.addAttribute(ConstantesSession.INFO, info);
        }
        model.addAttribute(ConstantesSession.CONNECTED, session.getAttribute(ConstantesSession.EMAIL) != null);
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
            case Constantes.PSEUDO_EXISTE_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.PSEUDO_EXISTE_EN : Constantes.PSEUDO_EXISTE_FR);
            case Constantes.COMPTE_ERROR_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.COMPTE_ERROR_EN : Constantes.COMPTE_ERROR_FR);
            case Constantes.PASSWORD_DIFFERENT_KEY ->
                    (langue == Constantes.CODE_ANGLAIS ? Constantes.PASSWORD_DIFFERENT_EN : Constantes.PASSWORD_DIFFERENT_FR);
            case Constantes.EMAIL_NON_ACCEPTE_KEY ->
                    Constantes.EMAIL_NON_ACCEPTE_FR;
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

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }

}
