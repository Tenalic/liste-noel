package sc.liste.noel.liste_noel.back.db.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "liste")
public class ListeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liste")
    private Long idListe;
    @Column(name = "email")
    private String proprietaire;
    @Column(name = "nom_liste")
    private String nomListe;
    @OneToMany(mappedBy = "idListe", cascade = CascadeType.ALL)
    private List<ObjetEntity> objetEntityList;

    public ListeEntity() {
    }

    public Long getIdListe() {
        return idListe;
    }

    public void setIdListe(Long idListe) {
        this.idListe = idListe;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public List<ObjetEntity> getObjetDaoList() {
        return objetEntityList;
    }

    public void setObjetDaoList(List<ObjetEntity> objetEntityList) {
        this.objetEntityList = objetEntityList;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }
}
