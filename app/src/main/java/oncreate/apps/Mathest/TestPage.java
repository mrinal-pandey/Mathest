package oncreate.apps.Mathest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestPage extends AppCompatActivity {

    public class Classifier extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            String result = "";
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1)
                {
                    char current = (char)data;
                    result = result + current;
                    data = reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Classifier: ", s);
        }
    }

    public class Downloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1)
                {
                    char current = (char)data;
                    result = result + current;
                    data = reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                Log.i("JSON content", s);
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); ++i) {

                    JSONObject details = jsonArray.getJSONObject(i);
                    number1 = details.getString("number1");
                    number2 = details.getString("number2");
                }
                questionNumber.setText("Question " + (nextQuestion + 1));
                switch (sheetNo){
                    case 1:
                        questionBody.setText("Add " + number1 + " and " + number2);
                        break;
                    case 2:
                        questionBody.setText("Subtract " + number2 + " from " + number1);
                        break;
                    case 3:
                        questionBody.setText("Mutliply " + number1 + " with " + number2);
                        break;
                    case 4:
                        questionBody.setText("Divide " + number1 + " by " + number2);
                        break;
                }
            }
            catch (Exception e)
            {

            }

        }
    }

    FirebaseFirestore firebaseFirestore;
    TextView questionNumber;
    TextView questionBody;
    EditText userAnswer;
    int nextQuestion;
    int sheetNo;
    String UID;
    String number1 = "", number2 = "";
    int correctAnswersCounter = 0;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TestPage.this)
                .setMessage("You can't go back from here")
                .setTitle("Please press Finish test to exit")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        Intent intent = getIntent();
        nextQuestion = intent.getIntExtra("nextQuestion", 0);
        sheetNo = intent.getIntExtra("sheetNo", 0);
        UID = intent.getStringExtra("UID");
        correctAnswersCounter = intent.getIntExtra("correctAnswers", 0);

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Toast.makeText(this, "Sheet No :" + sheetNo + "nq: " + nextQuestion + "uid: " + UID, Toast.LENGTH_LONG).show();

        questionBody = findViewById(R.id.questionBody_txt);
        questionNumber = findViewById(R.id.questionNumber_txt);
        userAnswer = findViewById(R.id.userAnswer_edittxt);

        getQuestionDetails(nextQuestion);

    }

    public void getQuestionDetails(int nextQuestion) {

        Downloader task = new Downloader();
        task.execute("http://mathest.herokuapp.com/question?uid="+UID+"&row="+(nextQuestion+2)+"&sno="+sheetNo);

    }

    public void nextQuestion(View view) {
        //check if correct, ask new question and update total questions in firestore.
        String message = "";
        Classifier classifier = new Classifier();
        switch (sheetNo){
            case 1:
                if (Integer.valueOf(number1) + Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute("http://mathest.herokuapp.com/addition?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 2:
                if (Integer.valueOf(number1) - Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute("http://mathest.herokuapp.com/subtraction?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 3:
                if (Integer.valueOf(number1) * Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute("http://mathest.herokuapp.com/multiplication?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 4:
                if (Integer.valueOf(number1) / Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute("http://mathest.herokuapp.com/division?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        userAnswer.getText().clear();
        userAnswer.setHint("Enter numeric answer");
        getQuestionDetails(++nextQuestion);
    }

    public void finishTest(View view){

        switch (sheetNo){
            case 1:
                firebaseFirestore.collection("users").document(UID)
                        .update("additionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("additionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 2:
                firebaseFirestore.collection("users").document(UID)
                        .update("subtractionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("subtractionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 3:
                firebaseFirestore.collection("users").document(UID)
                        .update("multiplicationQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("multiplicationCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 4:
                firebaseFirestore.collection("users").document(UID)
                        .update("divisionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("divisionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                goToPreviousActivity();
                            }
                        });
                break;
        }
    }

    public void goToPreviousActivity(){
        Intent intent = new Intent(this, StudentHome.class);
        intent.putExtra("uid", UID);
        startActivity(intent);
    }

}
