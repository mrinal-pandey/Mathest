/*Home Screen of the user which contains option to take a test in any of the
four categories namely addition, subtraction, multiplication, division.
This screen also displays the progress and the rate of accuracy of the user.
 */
package oncreate.apps.Mathest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //Function to check network connectivity of the user
    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        //To avoid glitches in the interface, restricting the screen orientation to portrait
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        UID = getIntent().getStringExtra("uid");

        //Adding custom toolbar
        toolBar = findViewById(R.id.student_home_toolbar);
        setSupportActionBar(toolBar);

        //Adding bottom navigation fot the four categories "+ - * /"
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);

        readyTxt = findViewById(R.id.ready_txt);

        //firebase cloud instance
        firebaseFirestore = FirebaseFirestore.getInstance();

        setAttributes(R.id.addition_icon);
    }

    //To calculate and display the accuracy and progress made by the user
    public void defineProgress(){
        accuracyTxt = findViewById(R.id.accuracy_percent_txt);
        progressTxt = findViewById(R.id.progress_txt);
        int accuracyPercent;
        if(totalAns == 0) {
            accuracyPercent = 100; //Initially accuracy is considered 100%
        }
        else {
            accuracyPercent = (int) (((float)correctAns / totalAns) * 100);
        }
        accuracyTxt.setText(getString(R.string.accuracy_display) + accuracyPercent + "%");
        progressTxt.setText(getString(R.string.progress_display) + totalAns+"/"+(10+wrongAns*3));
        accuracyBar = findViewById(R.id.accuracy_bar);
        progressBar = findViewById(R.id.progress_bar);
        accuracyBar.setMax(100);
        progressBar.setMax(10+(wrongAns*3));
        animateProgressBar(accuracyBar,accuracyPercent);
        animateProgressBar(progressBar,totalAns);
    }

    //Adding the automatic sliding animation to the accuracy and progress bars
    public void animateProgressBar(ProgressBar progressBar, int value){
        objectAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,value);
        objectAnimator.setDuration(3000);
        objectAnimator.start();
    }

    //Providing a button in the toolbar to navigate to student profile
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_home_profile_menu, menu);
        return true;
    }

    //Profile button functionality described here
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(R.id.profile_icon == item.getItemId()) {
            //Toast.makeText(this, "Display here", Toast.LENGTH_SHORT).show();
            if(isNetworkConnected()) {
                Intent intent = new Intent(this, StudentProfile.class);
                intent.putExtra("UID", UID);
                startActivity(intent);
            }else{
                Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Providing functionality to the bottom navigation buttons
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
                            toolBar.setTitle(getString(R.string.addition_category));
                            textAnimation(getString(R.string.add_instruction));
                            totalAns = m_user.getAdditionQuestionsAnswered();
                            correctAns = m_user.getAdditionCorrectAnswers();
                            sheetNo = 1;
                            break;
                        case R.id.subtraction_icon:
                            toolBar.setTitle(getString(R.string.subtraction_category));
                            textAnimation(getString(R.string.subtract_instruction));
                            totalAns = m_user.getSubtractionQuestionsAnswered();
                            correctAns = m_user.getSubtractionCorrectAnswers();
                            sheetNo = 2;
                            break;
                        case R.id.multiplication_icon:
                            toolBar.setTitle(getString(R.string.multiplication_category));
                            textAnimation(getString(R.string.multiply_instruction));
                            totalAns = m_user.getMultiplicationQuestionsAnswered();
                            correctAns = m_user.getMultiplicationCorrectAnswers();
                            sheetNo = 3;
                            break;
                        case R.id.division_icon:
                            toolBar.setTitle(getString(R.string.division_statistics));
                            textAnimation(getString(R.string.divide_instruction));
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

    //Rotating via x axis animation added to the text
    public void textAnimation(String operation){
        String textToShow = getString(R.string.ready_display) + operation + " ?";
        readyTxt.setText(textToShow);
        readyTxt.animate().setDuration(1000).rotationX(30);
    }

    //Providing intent to the test page with necessary details
    public void launchTest(View view) {
        if(isNetworkConnected()) {
            Intent intent = new Intent(this, TestPage.class);
            intent.putExtra("UID", UID);
            intent.putExtra("correctAnswers", correctAns);
            intent.putExtra("nextQuestion", totalAns);
            intent.putExtra("sheetNo", sheetNo);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
    }
}
