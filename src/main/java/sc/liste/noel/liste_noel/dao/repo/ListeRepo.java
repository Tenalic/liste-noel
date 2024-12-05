package sc.liste.noel.liste_noel.dao.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dao.entity.SecretDao;

import java.util.List;

public interface ListeRepo extends CrudRepository<ListeDao, Long> {

	List<ListeDao> findByProprietaire(String email);

	ListeDao findByIdListe(Long idListe);

}
