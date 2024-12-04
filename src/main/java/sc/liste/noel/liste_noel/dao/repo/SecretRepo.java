package sc.liste.noel.liste_noel.dao.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.dao.entity.SecretDao;

public interface SecretRepo extends CrudRepository<SecretDao, String> {

	SecretDao findByNomApplication(String nomApplication);

	SecretDao findBySecret(String secret);

}
