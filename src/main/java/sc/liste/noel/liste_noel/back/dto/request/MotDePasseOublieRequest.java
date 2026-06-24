package sc.liste.noel.liste_noel.back.dto.request;

public class MotDePasseOublieRequest {

    private String email;

    public MotDePasseOublieRequest() {
    }

    public MotDePasseOublieRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
