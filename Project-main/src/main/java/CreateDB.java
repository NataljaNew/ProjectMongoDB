import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.time.LocalDateTime;
import java.util.List;

public class CreateDB {
    public static void main(String[] args) {
        MongoClient client = MongoClientProvider.getClient();
        MongoDatabase database = client.getDatabase("ExamProject");
        MongoCollection<Student> collectionStudents = database.getCollection("students", Student.class);
        MongoCollection<Exam> collectionExams = database.getCollection("exams", Exam.class);
        MongoCollection<Question> collectionQuestions = database.getCollection("questions", Question.class);
        MongoCollection<Answer> collectionAnswers = database.getCollection("answers", Answer.class);
        MongoCollection<Result> collectionResults = database.getCollection("results", Result.class);

        client.close();
    }
}
