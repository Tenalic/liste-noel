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
            ObjetDto objetDto = daoToDto(objetDao);
            objetDtoList.add(objetDto);
        }
        objetDtoList.sort(((o1, o2) -> Math.toIntExact(o1.getIdObjet() - o2.getIdObjet()))); // trie part Id croissant
        return objetDtoList;
    }

    private static ObjetDto daoToDto(ObjetDao objetDao) {
        ObjetDto objetDto = new ObjetDto();
        objetDto.setDescription(objetDao.getDescription());
        objetDto.setDetenteur(objetDao.getDetenteur());
        objetDto.setPseudoDetenteur(objetDao.getPseudoDetenteur());
        objetDto.setUrl(objetDao.getUrl());
        objetDto.setEstPrit(objetDao.getEstPrit());
        objetDto.setTitre(objetDao.getTitre());
        objetDto.setIdObjet(objetDao.getIdObjet());
        // FIXME : faire ça proprement
        objetDto.setValuePriorite(objetDao.getPrioriteValue());
        objetDto.setPriorite(transcoPriorite(objetDao.getPrioriteValue()));
        return objetDto;
    }

    public static String transcoPriorite(Integer value) {
        switch (value) {
            case 1 :
                return "❤\uFE0F❤\uFE0F❤\uFE0F❤\uFE0F❤\uFE0F";
            case 2 :
                return "❤\uFE0F❤\uFE0F❤\uFE0F❤\uFE0F";
            case 3 :
                return "❤\uFE0F❤\uFE0F❤\uFE0F";
            case 4 :
                return "❤\uFE0F❤\uFE0F";
            case 5 :
                return "❤\uFE0F";
        }
        return "NULL";
    }
}
