package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.dao.entity.ObjetDao;
import sc.liste.noel.liste_noel.dto.ObjetDto;

import java.util.ArrayList;
import java.util.List;

public class ObjetMapper {


    public static List<ObjetDto> daosToDtos(List<ObjetDao> objetDaoList) {
        if(objetDaoList == null) {
            return null;
        }
        List<ObjetDto> objetDtoList = new ArrayList<>();
        for(ObjetDao objetDao : objetDaoList) {
            ObjetDto objetDto = new ObjetDto();
            objetDto.setDescription(objetDao.getDescription());
            objetDto.setDetenteur(objetDao.getDetenteur());
            objetDto.setUrl(objetDao.getUrl());
            objetDto.setEstPrit(objetDao.getEstPrit());
            objetDto.setTitre(objetDao.getTitre());
            objetDto.setIdObjet(objetDao.getIdObjet());
            objetDtoList.add(objetDto);
        }
        return objetDtoList;
    }
}
