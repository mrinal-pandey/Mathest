package oncreate.apps.Mathest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import oncreate.apps.Mathest.Wrappers.User;

public class StudentProfile extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;
    String UID;
    String nameOfUser;
    int gradeOfUser;
    String schoolOfUser;
    int additionQuestionsAnswered = 0, additionCorrectAnswers = 0;
    int subtractionQuestionsAnswered = 0, subtractionCorrectAnswers = 0;
    int multiplicationQuestionsAnswered = 0, multiplicationCorrectAnswers = 0;
    int divisionQuestionsAnswered = 0, divisionCorrectAnswers = 0;
    int additionWrongAnswers = 0;
    int subtractionWrongAnswers = 0;
    int multiplicationWrongAnswers = 0;
    int divisionWrongAnswers = 0;
    TextView nameOfUserTextView, gradeOfUserTextView, schoolOfUserTextView;
    TextView additionQuestionsAnsweredTextView, additionCorrectAnswersTextView;
    TextView subtractionQuestionsAnsweredTextView, subtractionCorrectAnswersTextView;
    TextView multiplicationQuestionsAnsweredTextView, multiplicationCorrectAnswersTextView;
    TextView divisionQuestionsAnsweredTextView, divisionCorrectAnswersTextView;
    TextView additionWrongAnswersTextView;
    TextView subtractionWrongAnswersTextView;
    TextView multiplicationWrongAnswersTextView;
    TextView divisionWrongAnswersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        toolbar = findViewById(R.id.student_profile_toolbar);
        setSupportActionBar(toolbar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        nameOfUserTextView = findViewById(R.id.user_name);
        gradeOfUserTextView = findViewById(R.id.user_grade);
        schoolOfUserTextView = findViewById(R.id.user_school);

        additionQuestionsAnsweredTextView = findViewById(R.id.additionQuestionsAnswered);
        additionCorrectAnswersTextView = findViewById(R.id.additionCorrectAnswers);
        additionWrongAnswersTextView = findViewById(R.id.additionWrongAnswers);

        subtractionQuestionsAnsweredTextView = findViewById(R.id.subtractionQuestionsAnswered);
        subtractionCorrectAnswersTextView = findViewById(R.id.subtractionCorrectAnswers);
        subtractionWrongAnswersTextView = findViewById(R.id.subtractionWrongAnswers);

        multiplicationQuestionsAnsweredTextView = findViewById(R.id.multiplicationQuestionsAnswered);
        multiplicationCorrectAnswersTextView = findViewById(R.id.multiplicationCorrectAnswers);
        multiplicationWrongAnswersTextView = findViewById(R.id.multiplicationWrongAnswers);

        divisionQuestionsAnsweredTextView = findViewById(R.id.multiplicationQuestionsAnswered);
        divisionCorrectAnswersTextView = findViewById(R.id.multiplicationCorrectAnswers);
        divisionWrongAnswersTextView = findViewById(R.id.divisionWrongAnswers);

        UID = getIntent().getStringExtra("UID");
        Log.i("UID: ", UID);
        if (!UID.isEmpty()) {
            firebaseFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    User m_user = documentSnapshot.toObject(User.class);

                    nameOfUser = m_user.getName();
                    gradeOfUser = m_user.getGrade();
                    schoolOfUser = m_user.getSchool();

                    additionQuestionsAnswered = m_user.getAdditionQuestionsAnswered();
                    additionCorrectAnswers = m_user.getAdditionCorrectAnswers();
                    additionWrongAnswers = additionQuestionsAnswered - additionCorrectAnswers;

                    subtractionQuestionsAnswered = m_user.getSubtractionQuestionsAnswered();
                    subtractionCorrectAnswers = m_user.getSubtractionCorrectAnswers();
                    subtractionWrongAnswers = subtractionQuestionsAnswered - subtractionCorrectAnswers;

                    multiplicationQuestionsAnswered = m_user.getMultiplicationQuestionsAnswered();
                    multiplicationCorrectAnswers = m_user.getMultiplicationCorrectAnswers();
                    multiplicationWrongAnswers = multiplicationQuestionsAnswered - multiplicationCorrectAnswers;

                    divisionQuestionsAnswered = m_user.getDivisionQuestionsAnswered();
                    divisionCorrectAnswers = m_user.getDivisionCorrectAnswers();
                    divisionWrongAnswers = divisionQuestionsAnswered - divisionCorrectAnswers;

                    nameOfUserTextView.setText("Name: " + nameOfUser);
                    gradeOfUserTextView.setText("Grade: " + gradeOfUser);
                    schoolOfUserTextView.setText("School: " + schoolOfUser);

                    additionQuestionsAnsweredTextView.setText("Questions Answered: " + additionQuestionsAnswered);
                    additionCorrectAnswersTextView.setText("Correct Answers: " + additionCorrectAnswers);
                    additionWrongAnswersTextView.setText("Wrong Answers: " + additionWrongAnswers);

                    subtractionQuestionsAnsweredTextView.setText("Questions Answered: " + subtractionQuestionsAnswered);
                    subtractionCorrectAnswersTextView.setText("Correct Answers: " + subtractionCorrectAnswers);
                    subtractionWrongAnswersTextView.setText("Wrong Answers: " + subtractionWrongAnswers);

                    multiplicationQuestionsAnsweredTextView.setText("Questions Answered: " + multiplicationQuestionsAnswered);
                    multiplicationCorrectAnswersTextView.setText("Correct Answers: " + multiplicationCorrectAnswers);
                    multiplicationWrongAnswersTextView.setText("Wrong Answers: " + multiplicationWrongAnswers);

                    divisionQuestionsAnsweredTextView.setText("Questions Answered: " + divisionQuestionsAnswered);
                    divisionCorrectAnswersTextView.setText("Correct Answers: " + divisionCorrectAnswers);
                    divisionWrongAnswersTextView.setText("Wrong Answers: " + divisionWrongAnswers);

                }
            });
        }
    }

}