package sc.liste.noel.liste_noel.front.dtos;

public class GeneriqueResponse {

    private String messageRetour;

    private int codeRetour;

    public GeneriqueResponse() {
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
