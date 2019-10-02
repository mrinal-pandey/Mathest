package oncreate.apps.Mathest.Wrappers;

public class User {

    String Name;
    String School;
    String ClassName;
    String sheetLink;
    int questions_answered = 0;

    public User() {
    }

    public User(String name, String sheetLink, String school, String Class) {
        this.Name = name;
        this.sheetLink = sheetLink;
        this.School = school;
        this.ClassName = Class;
    }

    public String getName() {
        return Name;
    }

    public String getSheetLink() {
        return sheetLink;
    }

    public int getQuestions_answered() {
        return questions_answered;
    }

    public String getSchool() {
        return School;
    }

    public String getClassName() {
        return ClassName;
    }
}
