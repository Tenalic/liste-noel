package sc.liste.noel.liste_noel.front.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TokenDto implements Serializable {

    private String token;

    private LocalDateTime tokenExpireDate;

    private String cossy;

    public TokenDto() {
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
