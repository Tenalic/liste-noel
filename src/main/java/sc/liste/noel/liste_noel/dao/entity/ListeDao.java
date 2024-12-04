package sc.liste.noel.liste_noel.dao.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "liste")
public class ListeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liste")
    private Long idListe;
    @Column(name = "email")
    private String proprietaire;
    @Column(name = "nom_liste")
    private String nomListe;
    @OneToMany(mappedBy = "idObjet")
    private List<ObjetDao> objetDaoList;

    public ListeDao() {
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

    public List<ObjetDao> getObjetDaoList() {
        return objetDaoList;
    }

    public void setObjetDaoList(List<ObjetDao> objetDaoList) {
        this.objetDaoList = objetDaoList;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }
}
