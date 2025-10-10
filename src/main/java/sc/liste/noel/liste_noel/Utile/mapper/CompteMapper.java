package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.db.entity.CompteEntity;
import sc.liste.noel.liste_noel.dto.CompteDto;

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
