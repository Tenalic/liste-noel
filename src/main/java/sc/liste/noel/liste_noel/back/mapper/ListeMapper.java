package sc.liste.noel.liste_noel.back.mapper;

import sc.liste.noel.liste_noel.back.db.entity.ListeEntity;
import sc.liste.noel.liste_noel.back.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListeMapper {

    public static List<ListeDto> entitiesToDtosSansListeObjet(List<ListeEntity> listeEntityList) {
        if (listeEntityList == null) {
            return null;
        }
        List<ListeDto> list = new ArrayList<>();
        for (ListeEntity listeEntity : listeEntityList) {
            ListeDto listeDto = mapping(listeEntity);
            list.add(listeDto);
        }
        return list;
    }


    public static ListeDto entityToDto(ListeEntity listeEntity) {
        if (listeEntity == null) {
            return null;
        }
        ListeDto listeDto = mapping(listeEntity);
        listeDto.setListeObjet(ObjetMapper.entitiesToDtos(listeEntity.getObjetDaoList()));
        return listeDto;
    }

    private static ListeDto mapping(ListeEntity listeEntity) {
        ListeDto listeDto = new ListeDto();
        listeDto.setNomListe(listeEntity.getNomListe());
        listeDto.setProprietaire(listeEntity.getProprietaire());
        listeDto.setIdListe(listeEntity.getIdListe());
        listeDto.setPublique(listeEntity.getPublique());
        listeDto.setNombreObjet(Optional.of(listeEntity).map(ListeEntity::getObjetEntityList).map(List::size).orElse(0));
        return listeDto;
    }

    public static String buildUrlPartage(String baseUrl, Long idListe) {
        return baseUrl + "/liste/" + idListe;
    }

}
