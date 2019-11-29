package oncreate.apps.Mathest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import oncreate.apps.Mathest.UI.CorrectAnswerDialogHandler;
import oncreate.apps.Mathest.UI.DialogHandler;
import oncreate.apps.Mathest.UI.WrongAnswerDialogHandler;


public class TestPage extends AppCompatActivity {

    private final String TAG = "TestPage.class";

    public class Classifier extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Starting classifier");
            if(message.equals("Correct!")){
                correctAnswerDialogHandler.showDialog();
            }else if(message.equals("Wrong!")){
                wrongAnswerDialogHandler.showDialog();
            }
        }

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
                Log.d(TAG, "Fetching classifier URL " + url);

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
                Log.d(TAG, "Error fetching classifier URL, check logs");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(message.equals("Correct!")) {
                correctAnswerDialogHandler.hideDialog();
            }else if(message.equals("Wrong!")) {
                wrongAnswerDialogHandler.hideDialog();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        feedbackMessage = details.getString("comment");
                        new AlertDialog.Builder(TestPage.this)
                                .setMessage(feedbackMessage)
                                .setTitle("Please read this!")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("Content: ", s);
            userAnswer.getText().clear();
            getQuestionDetails(++nextQuestion);
        }
    }

    public class Downloader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Launching Downloader");
            dialogHandler.showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            Log.d(TAG, "Entered doInBackground for Dowlaoder");
            try
            {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                Log.d(TAG, "Fetching Downloader URL:" + url);

                while(data!=-1)
                {
                    char current = (char)data;
                    result = result + current;
                    data = reader.read();
                }
                Log.d(TAG, "result is " + result);
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.d(TAG, "Error met");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "Execution successful");
            try {
                //Log.i("JSON content", s);
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); ++i) {

                    JSONObject details = jsonArray.getJSONObject(i);
                    if(details.has("error")){
                        questionsExhausted = true;
                        new AlertDialog.Builder(TestPage.this)
                                .setMessage("Congratulations! You have answered all questions of this category. Please press 'Okay' and then 'Save progress' to exit.")
                                .setTitle("Questions exhausted!")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }else {
                        number1 = details.getString("number1");
                        number2 = details.getString("number2");
                    }
                }
                dialogHandler.hideDialog();
                if(!questionsExhausted) {
                    questionNumber.setText("Question " + (nextQuestion + 1));
                    switch (sheetNo) {
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
                }else{
                    questionNumber.setText("Congratulations!");
                    questionBody.setText("You have mastered this category!");
                    userAnswer.setEnabled(false);
                    userAnswer.setBackgroundColor(getResources().getColor(R.color.disableColor));
                    submitButton.setEnabled(false);
                    submitButton.setBackgroundColor(getResources().getColor(R.color.disableColor));
                    workspaceButton.setEnabled(false);
                    workspaceButton.setBackgroundColor(getResources().getColor(R.color.disableColor));
                }
            }
            catch (Exception e)
            {

            }

        }
    }

    FirebaseFirestore firebaseFirestore;
    DialogHandler dialogHandler;
    CorrectAnswerDialogHandler correctAnswerDialogHandler;
    WrongAnswerDialogHandler wrongAnswerDialogHandler;
    TextView questionNumber;
    TextView questionBody;
    static EditText userAnswer;
    Button submitButton, workspaceButton;
    int nextQuestion;
    int sheetNo;
    String UID;
    String message = "";
    String feedbackMessage = "";
    String number1 = "", number2 = "";
    int correctAnswersCounter = 0;
    boolean questionsExhausted = false;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TestPage.this)
                .setMessage("You can't go back from here")
                .setTitle("Please press Save progress to exit")
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

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        nextQuestion = intent.getIntExtra("nextQuestion", 0);
        sheetNo = intent.getIntExtra("sheetNo", 0);
        UID = intent.getStringExtra("UID");
        correctAnswersCounter = intent.getIntExtra("correctAnswers", 0);

        firebaseFirestore = FirebaseFirestore.getInstance();

        dialogHandler = new DialogHandler(this);
        correctAnswerDialogHandler = new CorrectAnswerDialogHandler(this);
        wrongAnswerDialogHandler = new WrongAnswerDialogHandler(this);

        //Toast.makeText(this, "Sheet No :" + sheetNo + "nq: " + nextQuestion + "uid: " + UID, Toast.LENGTH_LONG).show();

        questionBody = findViewById(R.id.questionBodyWorkspace);
        questionNumber = findViewById(R.id.questionNumber_txt);
        userAnswer = findViewById(R.id.userAnswer_edittxt);
        submitButton = findViewById(R.id.submitButtonTestPage);
        workspaceButton = findViewById(R.id.buttonAdditionWorkspace);

        getQuestionDetails(nextQuestion);

    }

    public void getQuestionDetails(int nextQuestion) {

        Downloader task = new Downloader();
        task.execute(this.getString(R.string.mathest_azure_endpoint)+"question?uid="+UID+"&row="+(nextQuestion+2)+"&sno="+sheetNo);

    }

    public void nextQuestion(View view) {
        //check if correct, ask new question and update total questions in firestore.
        Classifier classifier = new Classifier();
        switch (sheetNo){
            case 1:
                if (Integer.valueOf(number1) + Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute(this.getString(R.string.mathest_azure_endpoint)+"addition?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 2:
                if (Integer.valueOf(number1) - Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute(this.getString(R.string.mathest_azure_endpoint)+"subtraction?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 3:
                if (Integer.valueOf(number1) * Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute(this.getString(R.string.mathest_azure_endpoint)+"multiplication?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

            case 4:
                if (Integer.valueOf(number1) / Integer.valueOf(number2) == Integer.valueOf(userAnswer.getText().toString())){
                    message = "Correct!";
                    ++correctAnswersCounter;
                }else {
                    message = "Wrong!";
                }
                classifier.execute(this.getString(R.string.mathest_azure_endpoint)+"division?uid="+UID+"&row="+(nextQuestion+2)+"&answer="+userAnswer.getText().toString());
                break;

        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void finishTest(View view){

        switch (sheetNo){
            case 1:
                dialogHandler.showDialog();
                firebaseFirestore.collection("users").document(UID)
                        .update("additionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("additionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                dialogHandler.hideDialog();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 2:
                dialogHandler.showDialog();
                firebaseFirestore.collection("users").document(UID)
                        .update("subtractionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("subtractionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                dialogHandler.hideDialog();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 3:
                dialogHandler.showDialog();
                firebaseFirestore.collection("users").document(UID)
                        .update("multiplicationQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("multiplicationCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                dialogHandler.hideDialog();
                                goToPreviousActivity();
                            }
                        });
                break;
            case 4:
                dialogHandler.showDialog();
                firebaseFirestore.collection("users").document(UID)
                        .update("divisionQuestionsAnswered", nextQuestion)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-1", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseFirestore.collection("users").document(UID).
                        update("divisionCorrectAnswers", correctAnswersCounter)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(TestPage.this, "Firebase updated-2", Toast.LENGTH_SHORT).show();
                                dialogHandler.hideDialog();
                                goToPreviousActivity();
                            }
                        });
                break;
        }
    }

    public void additionWorkspace(View view){
        if(sheetNo == 1) {
            Intent intent = new Intent(this, AdditionWorkspace.class);
            intent.putExtra("number1", number1);
            intent.putExtra("number2", number2);
            intent.putExtra("UID", UID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Functionality not available!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToPreviousActivity(){
        Intent intent = new Intent(this, StudentHome.class);
        intent.putExtra("uid", UID);
        startActivity(intent);
        finish();
    }

}
