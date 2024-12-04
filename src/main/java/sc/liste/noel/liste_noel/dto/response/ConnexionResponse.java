package sc.liste.noel.liste_noel.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ConnexionResponse implements Serializable {

    private String token;

    private LocalDateTime tokenExpireDate;

    private String cossy;

    private String messageRetour;

    private int codeRetour;

    public ConnexionResponse() {
    }

    public ConnexionResponse(String token) {
        this.token = token;
    }

    public ConnexionResponse(String token, String messageRetour, int codeRetour) {
        this.token = token;
        this.messageRetour = messageRetour;
        this.codeRetour = codeRetour;
    }

    public ConnexionResponse(String token, LocalDateTime tokenExpireDate, String messageRetour, int codeRetour) {
        this.token = token;
        this.tokenExpireDate = tokenExpireDate;
        this.messageRetour = messageRetour;
        this.codeRetour = codeRetour;
    }

    public ConnexionResponse(String token, LocalDateTime tokenExpireDate, String cossy, String messageRetour, int codeRetour) {
        this.token = token;
        this.tokenExpireDate = tokenExpireDate;
        this.cossy = cossy;
        this.messageRetour = messageRetour;
        this.codeRetour = codeRetour;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessageRetour() {
        return messageRetour;
    }

    public void setMessageRetour(String messageRetour) {
        this.messageRetour = messageRetour;
    }

    public int getCodeRetour() {
        return codeRetour;
    }

    public void setCodeRetour(int codeRetour) {
        this.codeRetour = codeRetour;
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
