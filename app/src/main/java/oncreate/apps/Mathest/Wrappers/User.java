package oncreate.apps.Mathest.Wrappers;

public class User {

    String name;
    String school;
    int grade;
    String sheetLink;
    int additionCorrectAnswers = 0;
    int additionQuestionsAnswered = 0;
    int subtractionCorrectAnswers = 0;
    int subtractionQuestionsAnswered = 0;
    int multiplicationCorrectAnswers = 0;
    int multiplicationQuestionsAnswered = 0;
    int divisionCorrectAnswers = 0;
    int divisionQuestionsAnswered = 0;

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

    public int getAdditionCorrectAnswers() {
        return additionCorrectAnswers;
    }

    public int getSubtractionCorrectAnswers() {return subtractionCorrectAnswers; }

    public int getMultiplicationCorrectAnswers() {return multiplicationCorrectAnswers; }

    public int getDivisionCorrectAnswers() {return divisionCorrectAnswers; }

    public String getSheetLink() {
        return sheetLink;
    }

    public int getAdditionQuestionsAnswered() {
        return additionQuestionsAnswered;
    }

    public int getSubtractionQuestionsAnswered() {return subtractionQuestionsAnswered; }

    public int getMultiplicationQuestionsAnswered() {return multiplicationQuestionsAnswered; }

    public int getDivisionQuestionsAnswered() {return divisionQuestionsAnswered; }

    public String getSchool() {
        return school;
    }

    public int getGrade() {
        return grade;
    }
}
