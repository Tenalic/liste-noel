package sc.liste.noel.liste_noel.dto;

import java.util.List;

public class CompteDto {

    private String email;

    private List<ListeDto> listDeListeDto;

    public CompteDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ListeDto> getListDeListe() {
        return listDeListeDto;
    }

    public void setListDeListe(List<ListeDto> listDeListeDto) {
        this.listDeListeDto = listDeListeDto;
    }
}
