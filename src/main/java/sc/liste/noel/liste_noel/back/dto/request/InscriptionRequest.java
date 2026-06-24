package sc.liste.noel.liste_noel.back.dto.request;

public class InscriptionRequest {
    private String pseudo;
    private String email;
    private String password;
    private String confirmPassword;
    private Boolean acceptCGU;

    public InscriptionRequest() {
    }

    public InscriptionRequest(String pseudo, String email, String password, String confirmPassword, Boolean acceptCGU) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.acceptCGU = acceptCGU;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getAcceptCGU() {
        return acceptCGU;
    }

    public void setAcceptCGU(Boolean acceptCGU) {
        this.acceptCGU = acceptCGU;
    }
}
