package sc.liste.noel.liste_noel.back;

import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;
import sc.liste.noel.liste_noel.common.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;

public class ListeMapper {

    public static List<ListeDto> entitiesToDtos(List<ListeEntity> listeEntityList) {
        if(listeEntityList == null) {
            return null;
        }
        List<ListeDto> list = new ArrayList<>();
        for(ListeEntity listeEntity : listeEntityList) {
            ListeDto listeDto = new ListeDto();
            listeDto.setNomListe(listeEntity.getNomListe());
            listeDto.setProprietaire(listeEntity.getProprietaire());
            listeDto.setIdListe(listeEntity.getIdListe());
            listeDto.setListeObjet(ObjetMapper.entitiesToDtos(listeEntity.getObjetDaoList()));
            list.add(listeDto);
        }
        return list;
    }


    public static ListeDto entityToDto(ListeEntity listeEntity) {
        if(listeEntity == null) {
            return null;
        }
        ListeDto listeDto = new ListeDto();
        listeDto.setNomListe(listeEntity.getNomListe());
        listeDto.setProprietaire(listeEntity.getProprietaire());
        listeDto.setIdListe(listeEntity.getIdListe());
        listeDto.setListeObjet(ObjetMapper.entitiesToDtos(listeEntity.getObjetDaoList()));
        return listeDto;
    }

    public static String buildUrlPartage(String baseUrl, Long idListe) {
        return baseUrl + "/partage?id=" + idListe;
    }

}
