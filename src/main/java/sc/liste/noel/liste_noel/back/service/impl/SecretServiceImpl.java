package sc.liste.noel.liste_noel.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.back.db.repo.SecretRepo;
import sc.liste.noel.liste_noel.back.service.SecretServiceInterface;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SecretServiceImpl implements SecretServiceInterface {

	private static final String NAME_MY_SECRET = "client-pairing";

	private String mySecret;

	@Autowired
	private SecretRepo secretRepo;

	private final Set<String> listSecretDejaAutorise = new HashSet<>();

	@Override
	public boolean verifierSecret(String secret) {
		if (listSecretDejaAutorise.contains(secret)) {
			return true;
		}
		if (Optional.ofNullable(secretRepo.findBySecret(secret)).isPresent()) {
			listSecretDejaAutorise.add(secret);
			return true;
		}
		return false;
	}

	@Override
	public String getMySecret() {
		if (mySecret == null) {
			mySecret = secretRepo.findByNomApplication(NAME_MY_SECRET).getSecret();
		}
		return mySecret;
	}

}
