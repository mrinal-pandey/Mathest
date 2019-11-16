package oncreate.apps.Mathest;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import oncreate.apps.Mathest.UI.DialogHandler;
import oncreate.apps.Mathest.Wrappers.User;

public class StudentHome extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    private final String TAG = "StudentHome";
    DialogHandler dialogHandler;
    String UID;
    int totalAns;
    int correctAns;
    int wrongAns;
    int sheetNo;

    TextView readyTxt, accuracyTxt, progressTxt;
    private Toolbar toolBar;
    private ProgressBar accuracyBar, progressBar;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        UID = getIntent().getStringExtra("uid");

        toolBar = findViewById(R.id.student_home_toolbar);
        setSupportActionBar(toolBar);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        readyTxt = findViewById(R.id.ready_txt);
        firebaseFirestore = FirebaseFirestore.getInstance();

        setAttributes(R.id.addition_icon);
    }

    public void defineProgress(){
        accuracyTxt = findViewById(R.id.accuracy_percent_txt);
        progressTxt = findViewById(R.id.progress_txt);
        int accuracyPercent;
        if(totalAns == 0) {
            accuracyPercent = 100;
        }
        else {
            accuracyPercent = (int) (((float)correctAns / totalAns) * 100);
        }
        accuracyTxt.setText("Your accuracy: " + accuracyPercent + "%");
        progressTxt.setText("Your progress: " + totalAns+"/"+(10+wrongAns*3));
        accuracyBar = findViewById(R.id.accuracy_bar);
        progressBar = findViewById(R.id.progress_bar);
        accuracyBar.setMax(100);
        progressBar.setMax(10+(wrongAns*3));
        animateProgressBar(accuracyBar,accuracyPercent);
        animateProgressBar(progressBar,totalAns);
    }

    public void animateProgressBar(ProgressBar progressBar, int value){
        objectAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,value);
        objectAnimator.setDuration(3000);
        objectAnimator.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_home_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.profile_icon == item.getItemId()) {
            //Toast.makeText(this, "Display here", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, StudentProfile.class);
            intent.putExtra("UID", UID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            setAttributes(menuItem.getItemId());
            menuItem.setChecked(true);
            return false;
        }
    };

    //the function is used to set the attributes based on the category (Addition, Subtraction, Division, Multiplication) selected by the user
    public void setAttributes(int receivedIconID){
        final int iconID = receivedIconID;
        if (!UID.isEmpty()) {
            dialogHandler = new DialogHandler(this);
            dialogHandler.showDialog();
            firebaseFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "Doc snapshot: " + documentSnapshot.toString());
                    User m_user = documentSnapshot.toObject(User.class);
                    switch (iconID) {
                        case R.id.addition_icon:
                            toolBar.setTitle("Addition");
                            textAnimation("Add");
                            totalAns = m_user.getAdditionQuestionsAnswered();
                            correctAns = m_user.getAdditionCorrectAnswers();
                            sheetNo = 1;
                            break;
                        case R.id.subtraction_icon:
                            toolBar.setTitle("Subtraction");
                            textAnimation("Subtract");
                            totalAns = m_user.getSubtractionQuestionsAnswered();
                            correctAns = m_user.getSubtractionCorrectAnswers();
                            sheetNo = 2;
                            break;
                        case R.id.multiplication_icon:
                            toolBar.setTitle("Multiplication");
                            textAnimation("Multiply");
                            totalAns = m_user.getMultiplicationQuestionsAnswered();
                            correctAns = m_user.getMultiplicationCorrectAnswers();
                            sheetNo = 3;
                            break;
                        case R.id.division_icon:
                            toolBar.setTitle("Division");
                            textAnimation("Divide");
                            totalAns = m_user.getDivisionQuestionsAnswered();
                            correctAns = m_user.getDivisionCorrectAnswers();
                            sheetNo = 4;
                            break;
                    }
                    wrongAns = totalAns - correctAns;
                    dialogHandler.hideDialog();
                    defineProgress();

                }
            });
        }
    }

    public void textAnimation(String operation){
        String textToShow = "Ready to " + operation + " ?";
        readyTxt.setText(textToShow);
        readyTxt.animate().setDuration(1000).rotationX(30);
    }

    public void launchTest(View view) {
        Intent intent = new Intent(this, TestPage.class);
        intent.putExtra("UID", UID);
        intent.putExtra("correctAnswers",correctAns);
        intent.putExtra("nextQuestion", totalAns);
        intent.putExtra("sheetNo", sheetNo);
        startActivity(intent);
        finish();
    }
}
