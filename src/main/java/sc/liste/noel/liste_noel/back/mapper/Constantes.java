package sc.liste.noel.liste_noel.back.mapper;

public class Constantes {

    public static final int RETOUR_API_OK = 0;

    public static final int RETOUR_API_KO = 1;

    public static final String MOT_DE_PASSE_OUBLIE_P1_KEY = "motDePasseOublie_P1";
    public static final String MOT_DE_PASSE_OUBLIE_P2_KEY = "motDePasseOublie_P2";

    // Messages de succès
    public static final String API_COMPTE_CREATION_SUCCES_KEY = "api.compte.creation.succes";
    public static final String API_COMPTE_SUPPRESSION_SUCCES_KEY = "api.compte.suppression.succes";
    public static final String API_COMPTE_PASSWORD_UPDATE_SUCCES_KEY = "api.compte.password.update.success";

    // Messages d'erreur (champs obligatoires)
    public static final String API_COMPTE_EMAIL_OBLIGATOIRE_KEY = "api.compte.email.obligatoire";
    public static final String API_COMPTE_PASSWORD_OBLIGATOIRE = "api.compte.password.obligatoire";

    // Messages d'erreur (technique)
    public static final String API_LISTE_ERREUR_KEY = "api.liste.erreur";


    // Messages d'erreur (logique métier)
    public static final String API_COMPTE_PASSWORD_DIFFERENT_KEY = "api.compte.password.different";
    public static final String API_COMPTE_SUPPRESSION_ECHEC_KEY = "api.compte.suppression.echec";
    public static final String API_COMPTE_ACTIVATION_SUCCES_KEY = "api.compte.activation.succes";
    public static final String API_COMPTE_ACTIVATION_ECHEC_KEY = "api.compte.activation.echec";
    public static final String API_SECRET_INVALID_KEY = "api.secret.invalid";
    public static final String API_ERROR_GENERIC_KEY = "api.error.generic";
    public static final String COMPTE_EXISTE_KEY = "compteExiste";
    public static final String PSEUDO_EXISTE_KEY = "pseudoExiste";
    public static final String COMPTE_ERROR_KEY = "compteError";
    public static final String CONNEXION_FAIL_KEY = "connexionFail";
    public static final String EMAIL_DESACTIVETED = "api.compte.mail.desactive";
    public static final String MDP_CHANGE_INCORECTE = "api.compte.mdp.change.incorecte";
    public static final String MDP_CHANGE_INTROUVABLE = "api.compte.mdp.change.introuvable";
    public static final String COMPTE_INTROUVABLE = "api.compte.introuvable";
    public static final String LISTE_INTROUVABLE = "api.liste.notfound";
    public static final String MODIFICATION_INTERDITE = "api.modification.interdit";
    public static final String SUPPRESSION_INTERDITE = "api.liste.suppression.interdit";



}
