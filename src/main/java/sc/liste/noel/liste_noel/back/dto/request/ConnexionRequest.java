package sc.liste.noel.liste_noel.back.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import static sc.liste.noel.liste_noel.back.Constantes.API_COMPTE_EMAIL_OBLIGATOIRE_KEY;
import static sc.liste.noel.liste_noel.back.Constantes.API_COMPTE_PASSWORD_OBLIGATOIRE;

public class ConnexionRequest {

    @NotBlank(message = API_COMPTE_EMAIL_OBLIGATOIRE_KEY)
    @Email
    private String email;

    @NotBlank(message = API_COMPTE_PASSWORD_OBLIGATOIRE)
    private String password;

    // getters + setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
