import java.io.Serializable;

public class Question implements Serializable {
    private int klausimas;
    private String tekstas;
    private String atsakymasA;
    private String atsakymasB;
    private String atsakymasC;
    private String atsakymasD;
    private String atsakymasE;

    public Question() {
    }

    public Question(int klausimas, String tekstas, String atsakymasA, String atsakymasB, String atsakymasC, String atsakymasD, String atsakymasE) {
        this.klausimas = klausimas;
        this.tekstas = tekstas;
        this.atsakymasA = atsakymasA;
        this.atsakymasB = atsakymasB;
        this.atsakymasC = atsakymasC;
        this.atsakymasD = atsakymasD;
        this.atsakymasE = atsakymasE;
    }

    public int getKlausimas() {
        return klausimas;
    }

    public void setKlausimas(int klausimas) {
        this.klausimas = klausimas;
    }

    public String getTekstas() {
        return tekstas;
    }

    public void setTekstas(String tekstas) {
        this.tekstas = tekstas;
    }

    public String getAtsakymasA() {
        return atsakymasA;
    }

    public void setAtsakymasA(String atsakymasA) {
        this.atsakymasA = atsakymasA;
    }

    public String getAtsakymasB() {
        return atsakymasB;
    }

    public void setAtsakymasB(String atsakymasB) {
        this.atsakymasB = atsakymasB;
    }

    public String getAtsakymasC() {
        return atsakymasC;
    }

    public void setAtsakymasC(String atsakymasC) {
        this.atsakymasC = atsakymasC;
    }

    public String getAtsakymasD() {
        return atsakymasD;
    }

    public void setAtsakymasD(String atsakymasD) {
        this.atsakymasD = atsakymasD;
    }

    public String getAtsakymasE() {
        return atsakymasE;
    }

    public void setAtsakymasE(String atsakymasE) {
        this.atsakymasE = atsakymasE;
    }

    @Override
    public String toString() {
        return "Question{" +
                "klausimas=" + klausimas +
                ", tekstas='" + tekstas + '\'' +
                ", atsakymasA='" + atsakymasA + '\'' +
                ", atsakymasB='" + atsakymasB + '\'' +
                ", atsakymasC='" + atsakymasC + '\'' +
                ", atsakymasD='" + atsakymasD + '\'' +
                ", atsakymasE='" + atsakymasE + '\'' +
                '}';
    }
}
