package sc.liste.noel.liste_noel.service;

import sc.liste.noel.liste_noel.dto.CompteDto;
import sc.liste.noel.liste_noel.dto.TokenDto;
import sc.liste.noel.liste_noel.exception.CompteNotFoundException;
import sc.liste.noel.liste_noel.exception.TokenExpiredException;

public interface CompteServiceInterface {

    /**
     * Verifie si un compte avec le email donnee existe en base de données
     *
     * @param email : email compte
     * @return true si un compte existe déjà, false sinon
     */
    boolean compteExiste(String email);

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
    CompteDto connexion(String email, String password);

    /**
     * Sauvegarde un nouveau compte avec le email et password donné en base de
     * données
     *
     * @param email    : email joueur
     * @param password : mot de passe joueur
     * @return true si tout s'est bien passé
     */
    boolean creationCompte(String email, String password, boolean cguAccepted, String pseudo);

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
     * augmente le nombre de deconexion du compte
     *
     * @param email : email du compte
     * @return true si cela c'est bien passé, false sinon
     */
    boolean deconexion(String email);

    /**
     * Recupère le token d'un joueur
     *
     * @param email : email dont on souhaite récupérer le token
     * @return TokenDto
     */
    TokenDto getTokenDtoByEmail(String email);

    /**
     * Recupère le email a partir d'un token
     *
     * @param token : token
     * @return email
     */
    TokenDto getTokenDtoByToken(String token) throws CompteNotFoundException, TokenExpiredException;

    void genererMotDePasseEtEnvoyer(String email);

    boolean activateUser(String email, String key);
}
