package sc.liste.noel.liste_noel.back.dto;

import java.time.LocalDateTime;

public class ConnexionResponse extends GeneriqueResponse {

    private String token;

    private LocalDateTime tokenExpireDate;

    private String cossy;

    public ConnexionResponse() {
    }

    public ConnexionResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpireDate() {
        return tokenExpireDate;
    }

    public void setTokenExpireDate(LocalDateTime tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public String getCossy() {
        return cossy;
    }

    public void setCossy(String cossy) {
        this.cossy = cossy;
    }
}
