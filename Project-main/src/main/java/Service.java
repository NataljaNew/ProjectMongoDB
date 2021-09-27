import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
   Map<Integer, Student> mapStudents = new HashMap();
    Scanner sc = new Scanner(System.in);
    List<Answer> answers = new ArrayList<>();
    List<Question> examQuestions = new ArrayList<>();
    Map<Student, List<Answer>> studentAnswerMap = new HashMap<>();
    Exam exam = new Exam();
    private int numberOfQuestions = 0;
    List<Result> resultList = new ArrayList<>();
    LocalDateTime time;

    public void menu() {
        File directory = new File("Catalogue");
        if (!directory.exists()) {
            try {
                directory.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File answersFile = new File("Catalogue/answersFile.json");
        if (!answersFile.exists()) {
            try {
                answersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File studentAnswersFile = new File("Catalogue/studentAnswersFile.json");
        if (!studentAnswersFile.exists()) {
            try {
                studentAnswersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File studentScourFile = new File("Catalogue/studentScourFile.json");
        if (!studentScourFile.exists()) {
            try {
                studentScourFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        do {
            System.out.println(" __________________________________");
            System.out.println("|    [1]   teacher's account       |");
            System.out.println("|    [2]   student registration    |");
            System.out.println("|    [3]   log in to start " + Enums.EXAM);
            System.out.println("|    [4]   retake " + Enums.EXAM);
            System.out.println("|    [5]   exit                    |");
            System.out.println("|__________________________________|");
            System.out.println("Enter  [1], or [2], [3], [4] or [5] : ");
            String input = sc.next();
            switch (input) {
                case "1" -> {
                    System.out.println(" ___________________________");
                    System.out.println("| [1] create" + Enums.EXAM + "            |");
                    System.out.println("| [2] show student's results |");
                    System.out.println("| [3] go to main menu        |");
                    System.out.println("|____________________________|");
                    String input1 = sc.next();
                    switch (input1) {
                        case "1" -> {
                            registerExam();
                            System.out.println("Enter number of questions:");
                            numberOfQuestions = getValidNumber();
                            createQuestions();
                            createAnswers();
                            generateAnswerFile(exam, answersFile);
                        }
                        case "2" -> {
                            sort(resultList);
                            MongoClient client = MongoClientProvider.getClient();
                            MongoDatabase database = client.getDatabase("ExamProject");
                            MongoCollection<Result> collection = database.getCollection("results", Result.class);

                            FindIterable<Result> documents = collection.find();
                            Iterator<Result> iterator = documents.iterator();
                            while (iterator.hasNext()){
                                Result result = iterator.next();
                                System.out.println(result);
                            }
                            client.close();

                        }
                        case "3" -> menu();
                        default -> System.out.println("something went wrong, try again.");
                    }
                }
                case "2" -> {
                    System.out.println("*** User Registration ***");
                    registerStudent();
                }
                case "3" -> {
                    System.out.println("*** User Log in ***");
                    takeExam(studentAnswersFile);
                    generateAnswerFile(exam, studentScourFile, resultList);
                }
                case "4" -> {
                    logIN();
                    LocalDateTime newTime = time.plusDays(2);
                    LocalDateTime timeNow = LocalDateTime.now();
                    if (timeNow.isAfter(newTime)) {
                        takeExam(studentAnswersFile);
                        generateAnswerFile(exam, studentScourFile, resultList);
                    } else {
                        System.out.println("Your can retake your" + Enums.EXAM + " only after " + newTime);
                    }

                }
                case "5" -> System.exit(0);
                default -> System.out.println("something went wrong, try again.");
            }
        } while (true);
    }

    public void registerStudent() {
        int id = getUniqueID();
        System.out.println(registerStudentByID(id).toString());
        System.out.println("Number of students registered = " + mapStudents.size());
    }

    public void generateAnswerFile(Exam exam, File name) {
        List<Object> list = new ArrayList<>();
        list.add(exam);
        list.add(answers);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(name, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateAnswerFile(Exam exam, File name, Map<Student, List<Answer>> studentAnswerMap) {
        List<Object> list = new ArrayList<>();
        list.add(exam);
        list.add(studentAnswerMap);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(name, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateAnswerFile(Exam exam, File name, List<Result> resultList) {
        List<Object> list = new ArrayList<>();
        list.add(exam);
        list.add(resultList);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(name, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Student registerStudentByID(int id) {
        System.out.println("Enter your name: ");
        String name = sc.next();
        System.out.println("Enter your surname:");
        String surname = sc.next();
        Student student = new Student(id, name, surname);
        mapStudents.put(id, student);

        MongoClient client = MongoClientProvider.getClient();
        MongoDatabase database = client.getDatabase("ExamProject");
        MongoCollection<Student> collectionStudents = database.getCollection("students", Student.class);
        collectionStudents.insertOne(student);
        client.close();

        System.out.println("Registration successful");
        return student;
    }

    public int getUniqueID() {
        int id = 0;
        do {
            int idCheck = getValidNumber();
            if (mapStudents.get(idCheck) == null) {
                id = idCheck;
            } else {
                System.out.println("This ID already exists, please check your ID number and try again");
            }
        } while (id == 0);
        return id;
    }

    public int getValidNumber() {
        int idCheck = 0;
        System.out.println("Enter your number:");
        String number = sc.next();
        try {
            idCheck = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            System.out.println("Your number is invalid. Try again.");
            getValidNumber();
        }
        return idCheck;
    }

    public void logIN() {
        int logIDCheck = getValidNumber();
        if (mapStudents.get(logIDCheck) != null) {
            System.out.println("Login is successful");
            System.out.println(mapStudents.get(logIDCheck).toString());
        } else {
            System.out.println("Please check your ID or register.");
            menu();
        }
    }
    public int getLogInId() {
        int getLogInId = getValidNumber();
        if (mapStudents.get(getLogInId) != null) {
            System.out.println("Login is successful");
            System.out.println(mapStudents.get(getLogInId).toString());
            return getLogInId;
        } else {
            System.out.println("Please check your ID or register.");
            menu();
            return 0;
        }
    }

    public void startExam(Student student) {
        List<Answer> list = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            System.out.println(examQuestions.get(i));
            System.out.println("Press [a], [b], [c], [d], or [e]:");
            String answer = sc.next();
            if (answer.equals("a") || answer.equals("b") || answer.equals("c") || answer.equals("d") || answer.equals("e")) {
                list.add(new Answer(i, answer));
            } else {
                System.out.println("Something went wrong, try again.");
                i--;
            }
        }
        studentAnswerMap.put(student, list);
        int score = 0;
        for (int i = 0; i < answers.size(); i++) {
            if (list.get(i).getAtsakymas().equals(answers.get(i).getAtsakymas())) {
                score++;
            }
        }
        BigDecimal result = new BigDecimal((double) score * 100 / numberOfQuestions).setScale(0, RoundingMode.HALF_UP);
        System.out.println("Your scour is " + score + " out of " + numberOfQuestions + ". That is = " + result + "%");
        int mark = Math.round(Integer.parseInt(String.valueOf(result)) / 10);
        int studentScour = (int) Math.round(mark + 0.5);
        resultList.add(new Result(student,studentScour));

        MongoClient client = MongoClientProvider.getClient();
        MongoDatabase database = client.getDatabase("ExamProject");
        MongoCollection<Result> collectionResults = database.getCollection("results", Result.class);
        collectionResults.insertOne(new Result(student,studentScour));
        client.close();

    }

    public void createAnswers() {
        for (int i = 0; i < numberOfQuestions; i++) {
            System.out.println("Enter answer for question nr. " + (i + 1) + ". Enter [a], [b], [c], [d], or [e]");
            String answer = sc.next();
            if (answer.equals("a") || answer.equals("b") || answer.equals("c") || answer.equals("d") || answer.equals("e")) {
                answers.add(new Answer((i+1), answer));

                MongoClient client = MongoClientProvider.getClient();
                MongoDatabase database = client.getDatabase("ExamProject");
                MongoCollection<Answer> collectionAnswers = database.getCollection("answers", Answer.class);
                collectionAnswers.insertOne(new Answer((i+1), answer));
                client.close();
            } else {
                System.out.println("Something went wrong, try again.");
                i--;
            }
        }
        System.out.println(Enums.EXAM + " file was created successful");
    }

    public void registerExam() {
        System.out.println("Enter " + Enums.EXAM + "'s ID number:");
        int number = getValidNumber();
        System.out.println("Enter " + Enums.EXAM + "'s name: ");
        String name = sc.next();
        System.out.println("Enter " + Enums.EXAM + "'s type:");
        String type = sc.next();
        exam = new Exam(number, name, type);
        System.out.println(exam.toString());

        MongoClient client = MongoClientProvider.getClient();
        MongoDatabase database = client.getDatabase("ExamProject");
        MongoCollection<Exam> collection = database.getCollection("exams", Exam.class);
        collection.insertOne(exam);
        client.close();

        System.out.println(Enums.EXAM + " registered successful");
    }
    public void createQuestions() {
        Scanner newScanner = new Scanner(System.in);
        for (int i = 0; i < numberOfQuestions; i++) {
            System.out.println("Please enter question nr. " + (i + 1) + ":");
            String question = newScanner.nextLine();
            System.out.println("Please enter possible answer [a] : ");
            String answerA = newScanner.nextLine();
            System.out.println("Please enter possible answer [b] : ");
            String answerB = newScanner.nextLine();
            System.out.println("Please enter possible answer [c] : (if you don't need this option, press [x] ");
            String answer1 = newScanner.nextLine();
            String answerC = "";
            if (answer1.equals("x")) {
                answerC = null;
            } else {
                answerC = answer1;
            }
            System.out.println("Please enter possible answer [d] : (if you don't need this option, press [x] ");
            String answer2 = newScanner.nextLine();
            String answerD = "";
            if (answer2.equals("x")) {
                answerD = null;
            } else {
                answerD = answer2;
            }
            System.out.println("Please enter possible answer [e] : (if you don't need this option, press [x] ");
            String answer3 = newScanner.nextLine();
            String answerE = "";
            if (answer3.equals("x")) {
                answerC = null;
            } else {
                answerC = answer3;
            }
            examQuestions.add(new Question(i, question, answerA, answerB, answerC, answerD, answerE));
            MongoClient client = MongoClientProvider.getClient();
            MongoDatabase database = client.getDatabase("ExamProject");
            MongoCollection<Question> collectionQuestions = database.getCollection("questions", Question.class);
            collectionQuestions.insertOne(new Question(i, question, answerA, answerB, answerC, answerD, answerE));
            client.close();
        }
        System.out.println("Questions registered");
    }

    public void takeExam(File resultFile) {
        int key = getLogInId();
        Student myStudent = mapStudents.get(key);
        startExam(myStudent);
        generateAnswerFile(exam, resultFile, studentAnswerMap);
        time = LocalDateTime.now();
    }

    public void sort (List<Result> resultList){
       resultList.stream()
                .mapToInt(Result::getScour)
                .max();
        System.out.println(resultList.toString());
    }
}
