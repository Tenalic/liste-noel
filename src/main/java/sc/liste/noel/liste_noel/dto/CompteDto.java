package sc.liste.noel.liste_noel.dto;

import java.util.List;

public class CompteDto {

    private String email;
    private String pseudo;

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

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public List<ListeDto> getListDeListeDto() {
        return listDeListeDto;
    }

    public void setListDeListeDto(List<ListeDto> listDeListeDto) {
        this.listDeListeDto = listDeListeDto;
    }
}
