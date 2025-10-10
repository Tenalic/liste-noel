package sc.liste.noel.liste_noel.back.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;

public interface CompteRepo extends CrudRepository<CompteEntity, String> {

    CompteEntity findByEmail(String email);

    CompteEntity findByPseudo(String pseudo);

    CompteEntity findByEmailAndPassword(String email, String password);


}
