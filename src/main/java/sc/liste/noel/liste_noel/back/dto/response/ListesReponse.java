package sc.liste.noel.liste_noel.back.dto.response;

import sc.liste.noel.liste_noel.back.dto.ListeDto;

import java.util.List;

public class ListesReponse extends GeneriqueResponse {

    private List<ListeDto> lisOfListesCadeaux;

    public ListesReponse(String messageRetour, int codeRetour) {
        super(messageRetour, codeRetour);
    }

    public ListesReponse(String messageRetour, int codeRetour, List<ListeDto> lisOfListesCadeaux) {
        super(messageRetour, codeRetour);
        this.lisOfListesCadeaux = lisOfListesCadeaux;
    }

    public List<ListeDto> getLisOfListesCadeaux() {
        return lisOfListesCadeaux;
    }

    public void setLisOfListesCadeaux(List<ListeDto> lisOfListesCadeaux) {
        this.lisOfListesCadeaux = lisOfListesCadeaux;
    }
}
