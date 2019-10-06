package oncreate.apps.Mathest.Wrappers;

public class User {

    String name;
    String school;
    String className;
    String sheetLink;
    int questionsAnswered = 0;

    public User() {
    }

    public User(String name, String sheetLink, String school, String Class) {
        this.name = name;
        this.sheetLink = sheetLink;
        this.school = school;
        this.className = Class;
    }

    public String getName() {
        return name;
    }

    public String getSheetLink() {
        return sheetLink;
    }

    public int getQuestions_answered() {
        return questionsAnswered;
    }

    public String getSchool() {
        return school;
    }

    public String getClassName() {
        return className;
    }
}
