package sc.liste.noel.liste_noel.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TokenDto implements Serializable {

    private java.lang.String token;

    private LocalDateTime tokenExpireDate;

    private java.lang.String cossy;

    public TokenDto() {
    }

    public java.lang.String getToken() {
        return token;
    }

    public void setToken(java.lang.String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpireDate() {
        return tokenExpireDate;
    }

    public void setTokenExpireDate(LocalDateTime tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public java.lang.String getCossy() {
        return cossy;
    }

    public void setCossy(java.lang.String cossy) {
        this.cossy = cossy;
    }
}
