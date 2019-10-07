package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestPage extends AppCompatActivity {

    TextView questionNumber;
    TextView questionBody;
    int nextQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);
        Intent intent = getIntent();
        nextQuestion = intent.getIntExtra("nextQuestion", 0);
        questionBody = findViewById(R.id.questionBody_txt);
        questionNumber = findViewById(R.id.questionNumber_txt);


    }

    public void getQuestionDetails(int nextQuestion) {
     /*
      TODO method which calls the server, with the question to be asked next. Server returns the corresponding question, and whether it has been answered or not..
      Setting the text of question number and body here.
      */

        questionNumber.setText("Question " + nextQuestion);
    }

    public void previousQuestion(View view) {
        Intent intent = new Intent(this, TestPage.class);
        intent.putExtra("nextQuestion", nextQuestion - 1);
        startActivity(intent);
    }

    public void nextQuestion(View view) {
        //check if correct, ask new question and update total questions in firestore.
        Intent intent = new Intent(this, TestPage.class);
        intent.putExtra("nextQuestion", nextQuestion + 1);
        startActivity(intent);
    }
}
