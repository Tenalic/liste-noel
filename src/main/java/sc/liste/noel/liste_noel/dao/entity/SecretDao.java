package sc.liste.noel.liste_noel.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "secret")
public class SecretDao {
	
	@Id
	@Column(name = "nom_application")
	private String nomApplication;
	
	@Column(name = "secret")
	private String secret;

	public SecretDao() {
		super();
	}

	public String getNomApplication() {
		return nomApplication;
	}

	public void setNomApplication(String nomApplication) {
		this.nomApplication = nomApplication;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	

}
