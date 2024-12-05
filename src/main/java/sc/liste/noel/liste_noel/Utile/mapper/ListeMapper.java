package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;

public class ListeMapper {

    public static List<ListeDto> daosToDtos(List<ListeDao> listeDaoList) {
        if(listeDaoList == null) {
            return null;
        }
        List<ListeDto> list = new ArrayList<>();
        for(ListeDao listeDao : listeDaoList) {
            ListeDto listeDto = new ListeDto();
            listeDto.setNomListe(listeDao.getNomListe());
            listeDto.setProprietaire(listeDao.getProprietaire());
            listeDto.setIdListe(listeDao.getIdListe());
            listeDto.setListeObjet(ObjetMapper.daosToDtos(listeDao.getObjetDaoList()));
            list.add(listeDto);
        }
        return list;
    }


    public static ListeDto daoToDto(ListeDao listeDao) {
        if(listeDao == null) {
            return null;
        }
        ListeDto listeDto = new ListeDto();
        listeDto.setNomListe(listeDao.getNomListe());
        listeDto.setProprietaire(listeDao.getProprietaire());
        listeDto.setIdListe(listeDao.getIdListe());
        listeDto.setListeObjet(ObjetMapper.daosToDtos(listeDao.getObjetDaoList()));
        return listeDto;
    }

    public static String buildUrlPartage(String baseUrl, Long idListe) {
        return baseUrl + "/partage?id=" + idListe;
    }

}
