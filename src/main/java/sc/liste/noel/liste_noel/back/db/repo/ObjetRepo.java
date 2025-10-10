package sc.liste.noel.liste_noel.back.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.back.db.entity.ObjetEntity;

public interface ObjetRepo extends CrudRepository<ObjetEntity, Long> {

    ObjetEntity findByIdObjet(Long idObjet);

}
