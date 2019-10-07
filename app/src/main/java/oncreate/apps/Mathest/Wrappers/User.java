package oncreate.apps.Mathest.Wrappers;

public class User {

    String name;
    String school;
    int grade;
    String sheetLink;
    int correctAnswers = 0;
    int questionsAnswered = 0;

    public User() {
    }

    public User(String name, String sheetLink, String school, int grade) {
        this.name = name;
        this.sheetLink = sheetLink;
        this.school = school;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public String getSheetLink() {
        return sheetLink;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public String getSchool() {
        return school;
    }

    public int getGrade() {
        return grade;
    }
}
