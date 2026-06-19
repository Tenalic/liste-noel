package sc.liste.noel.liste_noel.back.service;

import sc.liste.noel.liste_noel.back.exception.MailServiceDesactivedException;
import sc.liste.noel.liste_noel.back.exception.MotDePasseException;
import sc.liste.noel.liste_noel.back.dto.CompteDto;
import sc.liste.noel.liste_noel.back.exception.CompteNotFoundException;

public interface CompteServiceInterface {

    /**
     * Verifie si un compte avec le email donnee existe en base de données
     *
     * @param email : email compte
     * @return true si un compte existe déjà, false sinon
     */
    boolean compteExiste(String email);

    String getPseudo(String email) throws CompteNotFoundException;

    /**
     * Verifie si un compte avec le pseudo donnee existe en base de données
     *
     * @param pseudo : pseudo compte
     * @return true si un compte existe déjà, false sinon
     */
    boolean pseudoExiste(String pseudo);

    /**
     * Verifie qu'un compte avec le email et password donnee existe en base de
     * données
     *
     * @param email    : email joueur
     * @param password : mot de passe joueur
     * @return true si un compte correspond à la combinaison email et mot de passe,
     * false sinon
     */
    CompteDto connexion(String email, String password) throws CompteNotFoundException;

    /**
     * Sauvegarde un nouveau compte avec le email et password donné en base de
     * données
     *
     * @param email    : email joueur
     * @param password : mot de passe joueur
     * @return true si tout s'est bien passé
     */
    String creationCompte(String email, String password, boolean cguAccepted, String pseudo);

    /**
     * Supprime en base de données le compte avec le email donné
     *
     * @param email joueur a supprimer
     * @return true si tout s'est bien passé
     */
    boolean supprimerCompte(String email);

    /**
     * Met a jour le password du compte
     *
     * @param email       : email compte
     * @param oldPassword : ancien mot de passe
     * @param newPassword : nouveau mot de passe
     * @return true si tout s'est bien passé
     */
    boolean updatePassword(String email, String oldPassword, String newPassword);

    /**
     * Met a jour le password du compte
     *
     * @param email       : email compte
     * @param oldPassword : ancien mot de passe
     * @param newPassword : nouveau mot de passe
     * @return true si tout s'est bien passé
     */
    boolean updatePassword(String email, String oldPassword, String newPassword, String confirmationNewPassword) throws CompteNotFoundException, MotDePasseException;


    /**
     * augmente le nombre de deconexion du compte
     *
     * @param email : email du compte
     * @return true si cela c'est bien passé, false sinon
     */
    boolean deconexion(String email);

    void genererMotDePasseEtEnvoyer(String email) throws MailServiceDesactivedException;

    boolean activateUser(String email, String key);
}
