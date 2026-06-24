package sc.liste.noel.liste_noel.back.service;

public interface SecretServiceInterface {

	/**
	 * Verifie que le secret donnée existe en base de donnée
	 * 
	 * @param secret : secret de l'application appelante
	 * @return true si existe, false sinon
	 */
	boolean verifierSecret(String secret);

}
