package sc.liste.noel.liste_noel.back.db.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favoris")
public class FavorisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favoris")
    private Long idFavoris;

    @Column(name = "id_liste")
    private Long idListe;

    @Column(name = "email")
    private String email;

    public FavorisEntity() {
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
