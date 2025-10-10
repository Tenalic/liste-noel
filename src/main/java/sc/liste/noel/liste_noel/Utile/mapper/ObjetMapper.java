package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.db.entity.ObjetEntity;
import sc.liste.noel.liste_noel.dto.ObjetDto;

import java.util.ArrayList;
import java.util.List;

public class ObjetMapper {


    public static List<ObjetDto> daosToDtos(List<ObjetEntity> objetEntityList) {
        if(objetEntityList == null) {
            return null;
        }
        List<ObjetDto> objetDtoList = new ArrayList<>();
        for(ObjetEntity objetEntity : objetEntityList) {
            ObjetDto objetDto = daoToDto(objetEntity);
            objetDtoList.add(objetDto);
        }
        objetDtoList.sort(((o1, o2) -> Math.toIntExact(o1.getIdObjet() - o2.getIdObjet()))); // trie part Id croissant
        return objetDtoList;
    }

    private static ObjetDto daoToDto(ObjetEntity objetEntity) {
        ObjetDto objetDto = new ObjetDto();
        objetDto.setDescription(objetEntity.getDescription());
        objetDto.setDetenteur(objetEntity.getDetenteur());
        objetDto.setPseudoDetenteur(objetEntity.getPseudoDetenteur());
        objetDto.setUrl(objetEntity.getUrl());
        objetDto.setEstPrit(objetEntity.getEstPrit());
        objetDto.setTitre(objetEntity.getTitre());
        objetDto.setIdObjet(objetEntity.getIdObjet());
        // FIXME : faire ça proprement
        objetDto.setValuePriorite(objetEntity.getPrioriteValue());
        objetDto.setPriorite(transcoPriorite(objetEntity.getPrioriteValue()));
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
