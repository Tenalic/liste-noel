package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.dao.entity.CompteDao;
import sc.liste.noel.liste_noel.dto.CompteDto;

public class CompteMapper {

    public static CompteDto DaoToDto(CompteDao compteDao) {
        if(compteDao == null) {
            return null;
        }
        CompteDto compteDto = new CompteDto();
        compteDto.setEmail(compteDao.getEmail());
        compteDto.setPseudo(compteDao.getPseudo());
        return compteDto;
    }
}
