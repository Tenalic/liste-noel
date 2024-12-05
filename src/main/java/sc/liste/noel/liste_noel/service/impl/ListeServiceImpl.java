package sc.liste.noel.liste_noel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.Utile.mapper.ListeMapper;
import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dao.entity.ObjetDao;
import sc.liste.noel.liste_noel.dao.repo.ListeRepo;
import sc.liste.noel.liste_noel.dao.repo.ObjetRepo;
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

import java.util.List;

@Service
public class ListeServiceImpl implements ListeServiceInterface {
    @Autowired
    private ListeRepo listeRepo;

    private ObjetRepo objetRepo;

    @Override
    public ListeDto creerListe(String proprietaire, String nomListe) {

        ListeDao listeDao = new ListeDao();
        listeDao.setNomListe(nomListe);
        listeDao.setProprietaire(proprietaire);

        try {
            listeRepo.save(listeDao);
            return new ListeDto();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<ListeDto> getListeOfEmail(String email) {
        List<ListeDao> listeDaoList = listeRepo.findByProprietaire(email);
        return ListeMapper.daosToDtos(listeDaoList);
    }

    @Override
    public ListeDto getListeById(String id) {
        ListeDao listeDao = listeRepo.findByIdListe(Long.valueOf(id));
        return ListeMapper.daoToDto(listeDao);
    }

    @Override
    public void ajouterObjet(String titre, String url, String description, String idListe, String proprietaire) {
        ObjetDao objetDao = new ObjetDao();
        objetDao.setDescription(description);
        objetDao.setIdListe(Long.valueOf(idListe));
        objetDao.setTitre(titre);
        objetDao.setEstPrit(false);
        objetDao.setUrl(url);
        objetDao.setDetenteur(proprietaire);
        objetRepo.save(objetDao);
    }
}
