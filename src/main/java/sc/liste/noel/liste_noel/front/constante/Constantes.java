package sc.liste.noel.liste_noel.front.constante;

public class Constantes {

    public static int RETOUR_API_OK = 0;

    public static int RETOUR_API_KO = 1;

    public static final String MOT_DE_PASSE_OUBLIE_P1_KEY = "motDePasseOublie_P1";
    public static final String MOT_DE_PASSE_OUBLIE_P2_KEY = "motDePasseOublie_P2";

    // Messages de succès
    public static final String API_COMPTE_CREATION_SUCCES_KEY = "api.compte.creation.succes";
    public static final String API_COMPTE_SUPPRESSION_SUCCES_KEY = "api.compte.suppression.succes";
    public static final String API_COMPTE_PASSWORD_UPDATE_SUCCES_KEY = "api.compte.password.update.success";

    // Messages d'erreur (champs obligatoires)
    public static final String API_COMPTE_EMAIL_OBLIGATOIRE_KEY = "api.compte.cossy.obligatoire";
    public static final String API_COMPTE_OLD_PASSWORD_OBLIGATOIRE_KEY = "api.compte.oldPassword.obligatoire";
    public static final String API_COMPTE_NEW_PASSWORD_TROP_COURT_KEY = "api.compte.newPassword.trop_court";

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
    public static final String CONNEXION_KEY = "connexion";
    public static final String MDP_NOT_EQUALS_KEY = "mdpNotEquals";
    public static final String CGU_NON_ACCEPTE_KEY = "cguNonAccepte";
    public static final String EMAIL_NON_ACCEPTE_KEY = "emailMalForme";
    public static final String NEW_MDP_NOT_EQUALS_KEY = "newMdpNotEquals";
    public static final String CONNEXION_FAIL_KEY = "connexionFail";
    public static final String ERREUR_GENERIQUE_KEY = "erreurGenerique";



}
