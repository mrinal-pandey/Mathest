package oncreate.apps.Mathest.Wrappers;

public class User {

    String name;
    String school;
    int grade;
    String sheetID;
    int additionCorrectAnswers;
    int additionQuestionsAnswered;
    int subtractionCorrectAnswers;
    int subtractionQuestionsAnswered;
    int multiplicationCorrectAnswers;
    int multiplicationQuestionsAnswered;
    int divisionCorrectAnswers;
    int divisionQuestionsAnswered;

    public User() {
    }

    public User(String name, String sheetID, String school, int grade) {
        this.name = name;
        this.sheetID = sheetID;
        this.school = school;
        this.grade = grade;
        additionCorrectAnswers = 0;
        additionQuestionsAnswered = 0;
        subtractionCorrectAnswers = 0;
        subtractionQuestionsAnswered = 0;
        multiplicationCorrectAnswers = 0;
        multiplicationQuestionsAnswered = 0;
        divisionCorrectAnswers = 0;
        divisionQuestionsAnswered = 0;
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

    public String getSheetID() {
        return sheetID;
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
