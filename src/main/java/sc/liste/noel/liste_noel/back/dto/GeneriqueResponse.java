package sc.liste.noel.liste_noel.back.dto;

import java.io.Serializable;

public class GeneriqueResponse implements Serializable {

    private String messageRetour;

    private int codeRetour;

    public GeneriqueResponse() {
    }

    public GeneriqueResponse(String messageRetour, int codeRetour) {
        this.messageRetour = messageRetour;
        this.codeRetour = codeRetour;
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
}
