package sc.liste.noel.liste_noel.front.mappers;

import sc.liste.noel.liste_noel.back.dto.GeneriqueResponse;

public class GeneriqueResponseMapper {

    public static sc.liste.noel.liste_noel.front.dtos.GeneriqueResponse dtoBackToDtoFront(GeneriqueResponse generiqueResponseBack) {
        if (generiqueResponseBack == null) {
            return null;
        }
        sc.liste.noel.liste_noel.front.dtos.GeneriqueResponse generiqueResponseFront = new sc.liste.noel.liste_noel.front.dtos.GeneriqueResponse();
        generiqueResponseFront.setCodeRetour(generiqueResponseBack.getCodeRetour());
        generiqueResponseFront.setMessageRetour(generiqueResponseBack.getMessageRetour());
        return generiqueResponseFront;
    }
}
