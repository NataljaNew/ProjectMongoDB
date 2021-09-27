import java.io.Serializable;
import java.util.Scanner;

public class Exam implements Serializable{
    private int examID;
    private String name;
    private String type;

    public Exam(int examID, String name, String type) {
        this.examID = examID;
        this.name = name;
        this.type = type;
    }

    public Exam() {
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "egzaminas: {" +
                "ID:" + examID +
                ", pavadinimas:'" + name + '\'' +
                ", tipas:'" + type + '\'' +
                '}';
    }
}
