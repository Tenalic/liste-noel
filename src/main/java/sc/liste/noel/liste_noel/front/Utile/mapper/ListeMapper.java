package sc.liste.noel.liste_noel.front.Utile.mapper;

import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;
import sc.liste.noel.liste_noel.front.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;

public class ListeMapper {

    public static List<ListeDto> daosToDtos(List<ListeEntity> listeEntityList) {
        if(listeEntityList == null) {
            return null;
        }
        List<ListeDto> list = new ArrayList<>();
        for(ListeEntity listeEntity : listeEntityList) {
            ListeDto listeDto = new ListeDto();
            listeDto.setNomListe(listeEntity.getNomListe());
            listeDto.setProprietaire(listeEntity.getProprietaire());
            listeDto.setIdListe(listeEntity.getIdListe());
            listeDto.setListeObjet(ObjetMapper.daosToDtos(listeEntity.getObjetDaoList()));
            list.add(listeDto);
        }
        return list;
    }


    public static ListeDto daoToDto(ListeEntity listeEntity) {
        if(listeEntity == null) {
            return null;
        }
        ListeDto listeDto = new ListeDto();
        listeDto.setNomListe(listeEntity.getNomListe());
        listeDto.setProprietaire(listeEntity.getProprietaire());
        listeDto.setIdListe(listeEntity.getIdListe());
        listeDto.setListeObjet(ObjetMapper.daosToDtos(listeEntity.getObjetDaoList()));
        return listeDto;
    }

    public static String buildUrlPartage(String baseUrl, Long idListe) {
        return baseUrl + "/partage?id=" + idListe;
    }

}
