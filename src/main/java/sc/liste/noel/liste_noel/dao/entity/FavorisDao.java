package sc.liste.noel.liste_noel.dao.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "favoris")
public class FavorisDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favoris")
    private Long idFavoris;

    @Column(name = "id_liste")
    private Long idListe;

    @Column(name = "email")
    private String email;

    public FavorisDao() {
    }

    public Long getIdFavoris() {
        return idFavoris;
    }

    public void setIdFavoris(Long idFavoris) {
        this.idFavoris = idFavoris;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdListe() {
        return idListe;
    }

    public void setIdListe(Long idListe) {
        this.idListe = idListe;
    }
}
