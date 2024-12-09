package sc.liste.noel.liste_noel.dao.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.dao.entity.CompteDao;
import sc.liste.noel.liste_noel.dao.entity.FavorisDao;

import java.util.List;

public interface FavorisRepo extends CrudRepository<FavorisDao, Long> {

    List<FavorisDao> findByEmail(String email);
    FavorisDao findByEmailAndIdListe(String email, Long idListe);

}
