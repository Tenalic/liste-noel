package sc.liste.noel.liste_noel.back.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.back.db.entity.FavorisEntity;

import java.util.List;

public interface FavorisRepo extends CrudRepository<FavorisEntity, Long> {

    List<FavorisEntity> findByEmail(String email);
    FavorisEntity findByEmailAndIdListe(String email, Long idListe);

    List<FavorisEntity> findByIdListe(Long idListe);

}
