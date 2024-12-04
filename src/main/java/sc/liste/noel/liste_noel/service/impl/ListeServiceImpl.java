package sc.liste.noel.liste_noel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sc.liste.noel.liste_noel.Utile.mapper.ListeMapper;
import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dao.repo.ListeRepo;
import sc.liste.noel.liste_noel.dto.ListeDto;
import sc.liste.noel.liste_noel.service.ListeServiceInterface;

import java.util.List;

@Service
public class ListeServiceImpl implements ListeServiceInterface {
    @Autowired
    private ListeRepo listeRepo;

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
        return ListeMapper.DaoToDto(listeDaoList);
    }
}
