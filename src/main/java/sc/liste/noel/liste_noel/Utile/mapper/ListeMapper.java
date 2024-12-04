package sc.liste.noel.liste_noel.Utile.mapper;

import sc.liste.noel.liste_noel.dao.entity.ListeDao;
import sc.liste.noel.liste_noel.dto.ListeDto;

import java.util.ArrayList;
import java.util.List;

public class ListeMapper {

    public static List<ListeDto> DaoToDto(List<ListeDao> listeDaoList) {
        if(listeDaoList == null) {
            return null;
        }
        List<ListeDto> list = new ArrayList<>();
        for(ListeDao listeDao : listeDaoList) {
            ListeDto listeDto = new ListeDto();
            listeDto.setNomListe(listeDao.getNomListe());
            listeDto.setProprietaire(listeDao.getProprietaire());
            // TODO : mapper à creer
            listeDto.setList(null);
            list.add(listeDto);
        }
        return list;
    }

}
