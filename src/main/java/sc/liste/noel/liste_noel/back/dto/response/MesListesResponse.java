package sc.liste.noel.liste_noel.back.dto.response;

import sc.liste.noel.liste_noel.back.dto.ListeDto;

import java.util.List;

public class MesListesResponse extends GeneriqueResponse {
    private List<ListeDto> listes;
    private List<ListeDto> favoris;

    public MesListesResponse() {
    }

    public MesListesResponse(List<ListeDto> listes, List<ListeDto> favoris) {
        super("Succès", 0); // 0 pour OK selon ta convention
        this.listes = listes;
        this.favoris = favoris;
    }

    public List<ListeDto> getListes() {
        return listes;
    }

    public void setListes(List<ListeDto> listes) {
        this.listes = listes;
    }

    public List<ListeDto> getFavoris() {
        return favoris;
    }

    public void setFavoris(List<ListeDto> favoris) {
        this.favoris = favoris;
    }
}
