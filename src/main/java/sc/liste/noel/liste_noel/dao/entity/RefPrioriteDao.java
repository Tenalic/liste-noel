package sc.liste.noel.liste_noel.dao.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "ref_priorite")
public class RefPrioriteDao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "value",nullable = false, unique = true)
    private Integer value;

    @Column(name = "libelle",nullable = false, length = 255)
    private String libelle;

    public RefPrioriteDao() {
    }

    // Getters et setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
