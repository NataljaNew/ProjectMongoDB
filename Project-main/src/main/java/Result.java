import java.io.Serializable;

public class Result implements Serializable {
    private Student student;
    private int scour;


    public Result(Student student,int scour) {
        this.student = student;
        this.scour = scour;

    }

    public Result() {
    }

    public int getScour() {
        return scour;
    }

    public void setScour(int scour) {
        this.scour = scour;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Result{" +
                "student=" + student +
                ", scour=" + scour +
                '}';
    }
}
