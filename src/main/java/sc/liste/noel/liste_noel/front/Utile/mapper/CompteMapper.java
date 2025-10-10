package sc.liste.noel.liste_noel.front.Utile.mapper;

import sc.liste.noel.liste_noel.back.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.front.dto.CompteDto;

public class CompteMapper {

    public static CompteDto DaoToDto(CompteEntity compteEntity) {
        if(compteEntity == null) {
            return null;
        }
        CompteDto compteDto = new CompteDto();
        compteDto.setEmail(compteEntity.getEmail());
        compteDto.setPseudo(compteEntity.getPseudo());
        return compteDto;
    }
}
