package sc.liste.noel.liste_noel.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.db.entity.SecretEntity;

public interface SecretRepo extends CrudRepository<SecretEntity, String> {

	SecretEntity findByNomApplication(String nomApplication);

	SecretEntity findBySecret(String secret);

}
