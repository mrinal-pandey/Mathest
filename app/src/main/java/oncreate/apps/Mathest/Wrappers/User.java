package oncreate.apps.Mathest.Wrappers;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    String name;
    String school;
    int grade;
    String UID;
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

    public User(String name, String sheetID, String school, int grade, String UID) {
        this.name = name;
        this.sheetID = sheetID;
        this.school = school;
        this.grade = grade;
        this.UID = UID;
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

    public String getUID() {
        return UID;
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

    protected User(Parcel in) {
        name = in.readString();
        school = in.readString();
        grade = in.readInt();
        sheetID = in.readString();
        additionCorrectAnswers = in.readInt();
        additionQuestionsAnswered = in.readInt();
        subtractionCorrectAnswers = in.readInt();
        subtractionQuestionsAnswered = in.readInt();
        multiplicationCorrectAnswers = in.readInt();
        multiplicationQuestionsAnswered = in.readInt();
        divisionCorrectAnswers = in.readInt();
        divisionQuestionsAnswered = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(school);
        dest.writeInt(grade);
        dest.writeString(sheetID);
        dest.writeInt(additionCorrectAnswers);
        dest.writeInt(additionQuestionsAnswered);
        dest.writeInt(subtractionCorrectAnswers);
        dest.writeInt(subtractionQuestionsAnswered);
        dest.writeInt(multiplicationCorrectAnswers);
        dest.writeInt(multiplicationQuestionsAnswered);
        dest.writeInt(divisionCorrectAnswers);
        dest.writeInt(divisionQuestionsAnswered);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}