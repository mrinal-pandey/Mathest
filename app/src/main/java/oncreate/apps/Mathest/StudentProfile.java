package oncreate.apps.Mathest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import oncreate.apps.Mathest.UI.DialogHandler;
import oncreate.apps.Mathest.Wrappers.User;

public class StudentProfile extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    DialogHandler dialogHandler;
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
    TextView userUIDTextView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dialogHandler = new DialogHandler(this);

        dialogHandler.showDialog();
        sharedPreferences = this.getSharedPreferences("usercontent", Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.student_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        divisionQuestionsAnsweredTextView = findViewById(R.id.divisionQuestionsAnswered);
        divisionCorrectAnswersTextView = findViewById(R.id.divisionCorrectAnswers);
        divisionWrongAnswersTextView = findViewById(R.id.divisionWrongAnswers);

        userUIDTextView = findViewById(R.id.user_uid);

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

                    dialogHandler.hideDialog();

                    userUIDTextView.setText(getString(R.string.uid_display) +" " + UID);
                    nameOfUserTextView.setText(getString(R.string.name_display) + " " + nameOfUser);
                    if(gradeOfUser != -1) {
                        gradeOfUserTextView.setText(getString(R.string.grade_display) + " " + gradeOfUser);
                    }else{
                        gradeOfUserTextView.setVisibility(View.GONE);
                    }
                    if(!schoolOfUser.equals(getString(R.string.na))) {
                        schoolOfUserTextView.setText(getString(R.string.school_display) + " " + schoolOfUser);
                    }else{
                        schoolOfUserTextView.setVisibility(View.GONE);
                    }

                    additionQuestionsAnsweredTextView.setText(getString(R.string.questions_answered_display) + " " + additionQuestionsAnswered);
                    additionCorrectAnswersTextView.setText(getString(R.string.correct_answers_display) + " " + additionCorrectAnswers);
                    additionWrongAnswersTextView.setText(getString(R.string.wrong_answers_display) + " " + additionWrongAnswers);

                    subtractionQuestionsAnsweredTextView.setText(getString(R.string.questions_answered_display) + " " + subtractionQuestionsAnswered);
                    subtractionCorrectAnswersTextView.setText(getString(R.string.correct_answers_display) + " " + subtractionCorrectAnswers);
                    subtractionWrongAnswersTextView.setText(getString(R.string.wrong_answers_display) + " " + subtractionWrongAnswers);

                    multiplicationQuestionsAnsweredTextView.setText(getString(R.string.questions_answered_display) + " " + multiplicationQuestionsAnswered);
                    multiplicationCorrectAnswersTextView.setText(getString(R.string.correct_answers_display) + " " + multiplicationCorrectAnswers);
                    multiplicationWrongAnswersTextView.setText(getString(R.string.wrong_answers_display) + " " + multiplicationWrongAnswers);

                    divisionQuestionsAnsweredTextView.setText(getString(R.string.questions_answered_display) + " " + divisionQuestionsAnswered);
                    divisionCorrectAnswersTextView.setText(getString(R.string.correct_answers_display) + " " + divisionCorrectAnswers);
                    divisionWrongAnswersTextView.setText(getString(R.string.wrong_answers_display) + " " + divisionWrongAnswers);

                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.signout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent in = new Intent(this, Login.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
                break;
        }
        return true;
    }

}