package sc.liste.noel.liste_noel.back;

import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.common.dto.CompteDto;

public class CompteMapper {

    public static CompteDto EntityToDto(CompteEntity compteEntity) {
        if(compteEntity == null) {
            return null;
        }
        CompteDto compteDto = new CompteDto();
        compteDto.setEmail(compteEntity.getEmail());
        compteDto.setPseudo(compteEntity.getPseudo());
        return compteDto;
    }
}
