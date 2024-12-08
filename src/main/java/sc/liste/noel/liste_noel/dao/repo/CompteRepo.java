package sc.liste.noel.liste_noel.dao.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.dao.entity.CompteDao;

public interface CompteRepo extends CrudRepository<CompteDao, String> {

    CompteDao findByEmail(String email);

    CompteDao findByPseudo(String pseudo);

    CompteDao findByEmailAndPassword(String email, String password);


}
