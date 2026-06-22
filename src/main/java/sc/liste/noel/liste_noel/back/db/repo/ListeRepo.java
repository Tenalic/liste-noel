package sc.liste.noel.liste_noel.back.db.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;

import java.util.List;

public interface ListeRepo extends CrudRepository<ListeEntity, Long> {

	List<ListeEntity> findByProprietaire(String email);

	List<ListeEntity> findByPublique(boolean publique);

	List<ListeEntity> findByPubliqueAndNomListeContainingIgnoreCase(boolean publique, String nomListe);

	ListeEntity findByIdListe(Long idListe);


}
