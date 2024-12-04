package sc.liste.noel.liste_noel.dao.repo;

import org.springframework.data.repository.CrudRepository;
import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dao.entity.ObjetDao;

public interface ObjetRepo extends CrudRepository<ObjetDao, Long> {

}
