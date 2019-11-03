package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import oncreate.apps.Mathest.Wrappers.User;


public class StudentHome extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    private final String TAG = "StudentHome";
    String name;
    int grade;
    String school;
    int totalAns;
    int correctAns;
    int wrongAns;
    int sheetNo;
    String UID;

    TextView nameTxt, classNameTxt, schoolTxt, questionAnsweredTxt, correctAnswersTxt, wrongAnswersTxt;
    private Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);
        toolBar = findViewById(R.id.student_home_toolbar);
        setSupportActionBar(toolBar);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        nameTxt = findViewById(R.id.name_txt);
        classNameTxt = findViewById(R.id.class_txt);
        schoolTxt = findViewById(R.id.school_txt);
        questionAnsweredTxt = findViewById(R.id.questionsAnswered_txt);
        correctAnswersTxt = findViewById(R.id.correctAnswers_txt);
        wrongAnswersTxt = findViewById(R.id.wrongAnswers_txt);

        firebaseFirestore = FirebaseFirestore.getInstance();
        UID = getIntent().getStringExtra("uid");
        if (!UID.isEmpty()) {
            firebaseFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "Doc snapshot: " + documentSnapshot.toString());
                    User m_user = documentSnapshot.toObject(User.class);
                    Log.d(TAG, "Class name: " + m_user.getGrade());

                    name = m_user.getName();
                    grade = m_user.getGrade();
                    school = m_user.getSchool();

                    nameTxt.setText(name);
                    classNameTxt.setText("Class: " + grade);
                    schoolTxt.setText("School: " + school);

                }
            });
        }
        setAttributes(R.id.addition_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_home_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.profile_icon == item.getItemId()){
            Toast.makeText(this, "Display here", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StudentProfile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            setAttributes(menuItem.getItemId());
            return false;
        }
    };

    //the function is used to set the attributes based on the category (Addition, Subtraction, Division, Multiplication) selected by the user
    public void setAttributes(int receivedIconID){
        String UID = getIntent().getStringExtra("uid");
        final int iconID = receivedIconID;
        if (!UID.isEmpty()) {
            firebaseFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "Doc snapshot: " + documentSnapshot.toString());
                    User m_user = documentSnapshot.toObject(User.class);
                    switch (iconID) {
                        case R.id.addition_icon:
                            toolBar.setTitle("Addition");
                            totalAns = m_user.getAdditionQuestionsAnswered();
                            correctAns = m_user.getAdditionCorrectAnswers();
                            sheetNo = 1;
                            break;
                        case R.id.subtraction_icon:
                            toolBar.setTitle("Subtraction");
                            totalAns = m_user.getSubtractionQuestionsAnswered();
                            correctAns = m_user.getSubtractionCorrectAnswers();
                            sheetNo = 2;
                            break;
                        case R.id.multiplication_icon:
                            toolBar.setTitle("Multiplication");
                            totalAns = m_user.getMultiplicationQuestionsAnswered();
                            correctAns = m_user.getMultiplicationCorrectAnswers();
                            sheetNo = 3;
                            break;
                        case R.id.division_icon:
                            toolBar.setTitle("Division");
                            totalAns = m_user.getDivisionQuestionsAnswered();
                            correctAns = m_user.getDivisionCorrectAnswers();
                            sheetNo = 4;
                            break;
                    }
                    wrongAns = totalAns - correctAns;
                    questionAnsweredTxt.setText("Questions Answered: " + totalAns);
                    correctAnswersTxt.setText("Correct Answers: " + correctAns);
                    wrongAnswersTxt.setText("Wrong Answers: " + wrongAns);

                }
            });
        }
    }

    public void launchTest(View view) {
        Intent intent = new Intent(this, TestPage.class);
        intent.putExtra("nextQuestion", totalAns);
        intent.putExtra("sheetNo", sheetNo);
        intent.putExtra("UID", UID);
        intent.putExtra("correctAnswers", correctAns);
        startActivity(intent);
    }
}
