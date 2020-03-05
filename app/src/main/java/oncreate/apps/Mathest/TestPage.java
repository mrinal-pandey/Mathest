/*Test screen which displays question corresponding to the user,
* accepts answer and validates it providing an appropriate
* feedback incase the answer provided was wrong
* Calls the RESTful API hosted on heroku for all the above functionality*/
package oncreate.apps.Mathest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
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

    private final String TAG = "TestPage.class"; //For debugging purpose

    public class Classifier extends AsyncTask<String, Void, String>{

        //To display the gif corresponding to the correct and wrong answer
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "Starting classifier");
            if(message.equals(getString(R.string.correct_prompt))){
                correctAnswerDialogHandler.showDialog();
            }else if(message.equals(getString(R.string.wrong_prompt))){
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
                Toast.makeText(TestPage.this,"Error trying to access classifier API, check logs",Toast.LENGTH_LONG).show();
                Log.d(TAG, "Error fetching classifier URL, check logs");
                e.printStackTrace();
            }

            return null;
        }

        //To hide the gif and provide the feedback to the user
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(message.equals(getString(R.string.correct_prompt))) {
                correctAnswerDialogHandler.hideDialog();
            }else if(message.equals(getString(R.string.wrong_prompt))) {
                wrongAnswerDialogHandler.hideDialog();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        Resources res = getResources();
                        String pkg = getPackageName();
                        feedbackMessage = res.getString(res.getIdentifier(details.getString("comment"),
                                "string",
                                pkg));
                        new AlertDialog.Builder(TestPage.this)
                                .setMessage(feedbackMessage)
                                .setTitle(getString(R.string.read_instruction))
                                .setPositiveButton(getString(R.string.okay_dialog_button), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(TestPage.this,"Error trying to access API, check logs",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            Log.i("Content: ", s);
            userAnswer.getText().clear();
            userAnswer.setHint(getString(R.string.hint_answer_edit_box));
            userAnswer.setHintTextColor(getResources().getColor(R.color.disableColor));
            answerEntered = false;
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
                //dialogHandler.hideDialog();

                e.printStackTrace();
                Log.d(TAG, "Error met"+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialogHandler.hideDialog();
            Log.d(TAG, "Execution successful");
            try {
                //Log.i("JSON content", s);
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i < jsonArray.length(); ++i) {

                    JSONObject details = jsonArray.getJSONObject(i);
                    if(details.has("error")){
                        questionsExhausted = true;
                        new AlertDialog.Builder(TestPage.this)
                                .setMessage(getString(R.string.question_exhaust_message))
                                .setTitle(getString(R.string.question_exhaust_title))
                                .setPositiveButton(getString(R.string.okay_dialog_button), new DialogInterface.OnClickListener() {
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
                    questionNumber.setText(getString(R.string.question_number_display) + " " + (nextQuestion + 1));
                    switch (sheetNo) {
                        case 1:
                            questionBody.setText(getString(R.string.add_instruction) + " " + number1 + " " + getString(R.string.and) + " " + number2);
                            break;
                        case 2:
                            questionBody.setText(getString(R.string.subtract_instruction) + " " + number2 + " " + getString(R.string.from) + " " + number1);
                            break;
                        case 3:
                            questionBody.setText(getString(R.string.multiply_instruction) + " " + number1 + " " + getString(R.string.with) + " " + number2);
                            break;
                        case 4:
                            questionBody.setText(getString(R.string.divide_instruction) + " " + number1 + " " + getString(R.string.by) + " " + number2);
                            break;
                    }
                }else{
                    questionNumber.setText(getString(R.string.congratulations_display));
                    questionBody.setText(getString(R.string.mastered_category_message));
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
                Toast.makeText(TestPage.this,"Error trying to access API, check logs",Toast.LENGTH_LONG).show();

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
                .setMessage(getString(R.string.back_press_message))
                .setTitle(getString(R.string.back_press_title))
                .setPositiveButton(getString(R.string.okay_dialog_button), new DialogInterface.OnClickListener() {
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

        questionBody = findViewById(R.id.questionBodyMultiplicationWorkspace);
        questionNumber = findViewById(R.id.questionNumber_txt);
        userAnswer = findViewById(R.id.userAnswer_edittxt);
        submitButton = findViewById(R.id.submitButtonTestPage);
        workspaceButton = findViewById(R.id.buttonAdditionWorkspace);

        userAnswer.setHintTextColor(getResources().getColor(R.color.disableColor));

        if(isNetworkConnected()) {
            getQuestionDetails(nextQuestion);
        }else{
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void getQuestionDetails(int nextQuestion) {

        Downloader task = new Downloader();
        task.execute(this.getString(R.string.mathest_heroku_endpoint)+"question?uid="+UID+"&row="+(nextQuestion+2)+"&sno="+sheetNo);

    }

    boolean answerEntered = false;

    public void nextQuestion(View view) {
        if(isNetworkConnected()) {
            //check if correct, ask new question and update total questions in firestore.
            String userAnswerText = userAnswer.getText().toString();
            if (userAnswerText.equals("")) {
                userAnswer.setHintTextColor(getResources().getColor(R.color.wrongAnswerColor));
                userAnswer.setHint(getString(R.string.provide_answer_hint));
            } else {
                answerEntered = true;
            }
            if (answerEntered) {
                Classifier classifier = new Classifier();
                switch (sheetNo) {
                    case 1:
                        if (Integer.valueOf(number1) + Integer.valueOf(number2) == Integer.valueOf(userAnswerText)) {
                            message = getString(R.string.correct_prompt);
                            ++correctAnswersCounter;
                        } else {
                            message = getString(R.string.wrong_prompt);
                        }
                        classifier.execute(this.getString(R.string.mathest_heroku_endpoint) + "addition?uid=" + UID + "&row=" + (nextQuestion + 2) + "&answer=" + userAnswerText);
                        break;

                    case 2:
                        if (Integer.valueOf(number1) - Integer.valueOf(number2) == Integer.valueOf(userAnswerText)) {
                            message = getString(R.string.correct_prompt);
                            ++correctAnswersCounter;
                        } else {
                            message = getString(R.string.wrong_prompt);
                        }
                        classifier.execute(this.getString(R.string.mathest_heroku_endpoint) + "subtraction?uid=" + UID + "&row=" + (nextQuestion + 2) + "&answer=" + userAnswerText);
                        break;

                    case 3:
                        if (Integer.valueOf(number1) * Integer.valueOf(number2) == Integer.valueOf(userAnswerText)) {
                            message = getString(R.string.correct_prompt);
                            ++correctAnswersCounter;
                        } else {
                            message = getString(R.string.wrong_prompt);
                        }
                        classifier.execute(this.getString(R.string.mathest_heroku_endpoint) + "multiplication?uid=" + UID + "&row=" + (nextQuestion + 2) + "&answer=" + userAnswerText);
                        break;

                    case 4:
                        if (Integer.valueOf(number1) / Integer.valueOf(number2) == Integer.valueOf(userAnswerText)) {
                            message = getString(R.string.correct_prompt);
                            ++correctAnswersCounter;
                        } else {
                            message = getString(R.string.wrong_prompt);
                        }
                        classifier.execute(this.getString(R.string.mathest_heroku_endpoint) + "division?uid=" + UID + "&row=" + (nextQuestion + 2) + "&answer=" + userAnswerText);
                        break;

                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
    }

    public void finishTest(View view){
        if(isNetworkConnected()) {
            switch (sheetNo) {
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
        }else{
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
    }

    public void additionWorkspace(View view){
        if(isNetworkConnected()) {
            if (sheetNo == 1) {
                Intent intent = new Intent(this, AdditionWorkspace.class);
                intent.putExtra("number1", number1);
                intent.putExtra("number2", number2);
                intent.putExtra("UID", UID);
                startActivity(intent);
            } else if (sheetNo == 3) {
                Intent intent = new Intent(this, MultiplicationWorkspace.class);
                intent.putExtra("number1", number1);
                intent.putExtra("number2", number2);
                intent.putExtra("UID", UID);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.functionality_unavailable), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
    }

    public void goToPreviousActivity(){
        Intent intent = new Intent(this, StudentHome.class);
        intent.putExtra("uid", UID);
        startActivity(intent);
        finish();
    }

}
