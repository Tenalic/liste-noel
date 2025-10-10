package sc.liste.noel.liste_noel.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.db.entity.ListeEntity;

import java.util.List;

public interface ListeRepo extends CrudRepository<ListeEntity, Long> {

	List<ListeEntity> findByProprietaire(String email);

	ListeEntity findByIdListe(Long idListe);

}
