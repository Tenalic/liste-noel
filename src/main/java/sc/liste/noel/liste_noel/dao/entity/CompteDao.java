package sc.liste.noel.liste_noel.dao.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compte")
public class CompteDao {

    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "nb_connexion")
    private Integer nbConnexion;
    @Column(name = "nb_deconnexion")
    private Integer nbDeconnexion;
    @Column(name = "nb_modification_mdp")
    private Integer nbModificationMdp;
    @Column(name = "date_derniere_connexion")
    private LocalDateTime dateDerniereConnexion;
    @Column(name = "date_derniere_deconnexion")
    private LocalDateTime dateDerniereDeconnexion;
    @Column(name = "date_derniere_modification_mdp")
    private LocalDateTime dateDerniereModificationMdp;

    @OneToMany(mappedBy = "proprietaire")
    private List<ListeDao> listeDeListeDao;

    @Column(name = "cgu_accepted")
    private Boolean cguAccepted;

    public CompteDao() {
        super();
    }

    public CompteDao(String email, String password, boolean cguAccepted) {
        super();
        this.email = email;
        this.password = password;
        this.nbConnexion = 1;
        this.nbDeconnexion = 0;
        this.nbModificationMdp = 0;
        this.setDateDerniereConnexion(LocalDateTime.now());
        this.cguAccepted = cguAccepted;
    }

    public CompteDao(String email, String password, Integer initInteger) {
        super();
        this.email = email;
        this.password = password;
        this.nbConnexion = initInteger;
        this.nbDeconnexion = initInteger;
        this.nbModificationMdp = initInteger;
    }

    public CompteDao(String email, String password, Integer nbConnexion, Integer nbDeconnexion, Integer nbModificationMdp) {
        this.email = email;
        this.password = password;
        this.nbConnexion = nbConnexion;
        this.nbDeconnexion = nbDeconnexion;
        this.nbModificationMdp = nbModificationMdp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNbConnexion() {
        return nbConnexion;
    }

    public void setNbConnexion(Integer nbConnexion) {
        this.nbConnexion = nbConnexion;
    }

    public Integer getNbDeconnexion() {
        return nbDeconnexion;
    }

    public void setNbDeconnexion(Integer nbDeconnexion) {
        this.nbDeconnexion = nbDeconnexion;
    }

    public Integer getNbModificationMdp() {
        return nbModificationMdp;
    }

    public void setNbModificationMdp(Integer nbModificationMdp) {
        this.nbModificationMdp = nbModificationMdp;
    }

    public LocalDateTime getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }

    public void setDateDerniereConnexion(LocalDateTime dateDerniereConnexion) {
        this.dateDerniereConnexion = dateDerniereConnexion;
    }

    public LocalDateTime getDateDerniereDeconnexion() {
        return dateDerniereDeconnexion;
    }

    public void setDateDerniereDeconnexion(LocalDateTime dateDerniereDeconnexion) {
        this.dateDerniereDeconnexion = dateDerniereDeconnexion;
    }

    public LocalDateTime getDateDerniereModificationMdp() {
        return dateDerniereModificationMdp;
    }

    public void setDateDerniereModificationMdp(LocalDateTime dateDerniereModificationMdp) {
        this.dateDerniereModificationMdp = dateDerniereModificationMdp;
    }

    public Boolean getCguAccepted() {
        return cguAccepted;
    }

    public void setCguAccepted(Boolean cguAccepted) {
        this.cguAccepted = cguAccepted;
    }

    public List<ListeDao> getListeDeListeDao() {
        return listeDeListeDao;
    }

    public void setListeDeListeDao(List<ListeDao> listeDeListeDao) {
        this.listeDeListeDao = listeDeListeDao;
    }
}
