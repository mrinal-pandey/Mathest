/*Editor screen for addition
* */
package oncreate.apps.Mathest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdditionWorkspace extends AppCompatActivity {

    // Downloading the working of addition returned by the Mathest API
    public class Downloader extends AsyncTask<String, Void, String>{

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
            //Log.i("JSON", s);
            try{

                // Setting all the parameters as returned by API
                JSONArray jsonArray = new JSONArray(s);
                JSONObject carryDetails = jsonArray.getJSONObject(1);
                JSONObject number1Details = jsonArray.getJSONObject(2);
                JSONObject number2Details = jsonArray.getJSONObject(3);
                JSONObject resultDetails = jsonArray.getJSONObject(4);
                for(int i = 0; i < 4; ++i) {
                    carryArray[i] = Integer.valueOf(carryDetails.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 3; ++i) {
                    number1Array[i] = Integer.valueOf(number1Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 3; ++i) {
                    number2Array[i] = Integer.valueOf(number2Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 4; ++i) {
                    resultArray[i] = Integer.valueOf(resultDetails.getString("digit" + (i + 1)));
                }

                /*for(int i = 0; i < 4; ++i){
                    System.out.print(carryArray[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 3; ++i){
                    System.out.print(number1Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 3; ++i){
                    System.out.print(number2Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 4; ++i){
                    System.out.print(resultArray[i] + " ");
                }
                System.out.println();*/

            }catch (Exception e){

            }
        }
    }

    String number1;
    String number2;
    String UID;
    TextView questionBodyTextView;
    int[] carryArray = {0, 0, 0, 0};
    int[] number1Array = {-1, -1, -1};
    int[] number2Array = {-1, -1, -1};
    int[] resultArray = {-1, -1, -1, -1};
    EditText carry1, carry2, carry3, carry4;
    EditText number11, number12, number13;
    EditText number21, number22, number23;
    EditText answer1, answer2, answer3, answer4;
    int[] carryUserArray = {0, 0, 0, 0};
    int[] number1UserArray = {-1, -1, -1};
    int[] number2UserArray = {-1, -1, -1};
    int[] resultUserArray = {-1, -1, -1, -1};
    int userAnswer = 0;
    int wrongCount1 = 0, wrongCount2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_workspace);

        questionBodyTextView = findViewById(R.id.questionBodyMultiplicationWorkspace);

        // Getting the two numbers to work upon via Intent
        Intent intent = getIntent();
        number1 = intent.getStringExtra("number1");
        number2 = intent.getStringExtra("number2");
        UID = intent.getStringExtra("UID");

        questionBodyTextView.setText("Add " + number1 + " and " + number2);

        // If network is available download the working from API
        if(isNetworkConnected()) {
            Downloader downloader = new Downloader();
            downloader.execute(this.getString(R.string.mathest_heroku_endpoint) + "addition-working?uid=" + UID + "&number1=" + number1 + "&number2=" + number2);
        }else{
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
        }

        carry1 = findViewById(R.id.carry_digit_1);
        carry2 = findViewById(R.id.carry_digit_2);
        carry3 = findViewById(R.id.carry_digit_3);
        carry4 = findViewById(R.id.carry_digit_4);
        number11 = findViewById(R.id.number1_digit1);
        number12 = findViewById(R.id.number1_digit2);
        number13 = findViewById(R.id.number1_digit3);
        number21 = findViewById(R.id.number2_digit1);
        number22 = findViewById(R.id.number2_digit2);
        number23 = findViewById(R.id.number2_digit3);
        answer1 = findViewById(R.id.answer_digit1);
        answer2 = findViewById(R.id.answer_digit2);
        answer3 = findViewById(R.id.answer_digit3);
        answer4 = findViewById(R.id.answer_digit4);
    }

    // To set user answer on Test screen when back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(userAnswer != 0){
            TestPage.userAnswer.setText(String.valueOf(userAnswer));
        }
    }

    // To enable the question digit by setting it to black
    public void questionEnable(View view){
        view.setBackground(getResources().getDrawable(R.drawable.question_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    // To enable the answer digit by setting it to green
    public void answerEnable(View view){
        view.setBackground(getResources().getDrawable(R.drawable.answer_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    // Repaint the whole screen
    public void repaint(View view){
        Intent intent = new Intent(this, AdditionWorkspace.class);
        intent.putExtra("number1", number1);
        intent.putExtra("number2", number2);
        intent.putExtra("UID", UID);
        finish();
        startActivity(intent);
    }

    // Check the user working with the working of API whether it is correct or not
    public void checkBlocks(View view){

        if(!isNetworkConnected()){
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
            return;
        }

        boolean answerCorrectFlag = true, workingCorrectFlag = true;

        if(noAnswerEntered()){
            Toast.makeText(this, getString(R.string.provide_answer_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        // Get the user working in respective variables
        if(carry1.getText().toString().equals("")){
            carryUserArray[0] = 0;
        }else {
            carryUserArray[0] = Integer.valueOf(carry1.getText().toString());
        }

        if(carry2.getText().toString().equals("")){
            carryUserArray[1] = 0;
        }else {
            carryUserArray[1] = Integer.valueOf(carry2.getText().toString());
        }

        if(carry3.getText().toString().equals("")){
            carryUserArray[2] = 0;
        }else {
            carryUserArray[2] = Integer.valueOf(carry3.getText().toString());
        }

        if(carry4.getText().toString().equals("")){
            carryUserArray[3] = 0;
        }else {
            carryUserArray[3] = Integer.valueOf(carry4.getText().toString());
        }

        if(number11.getText().toString().equals("")){
            number1UserArray[0] = -1;
        }else{
            number1UserArray[0] = Integer.valueOf(number11.getText().toString());
        }

        if(number12.getText().toString().equals("")){
            number1UserArray[1] = -1;
        }else{
            number1UserArray[1] = Integer.valueOf(number12.getText().toString());
        }

        if(number13.getText().toString().equals("")){
            number1UserArray[2] = -1;
        }else{
            number1UserArray[2] = Integer.valueOf(number13.getText().toString());
        }

        if(number21.getText().toString().equals("")){
            number2UserArray[0] = -1;
        }else{
            number2UserArray[0] = Integer.valueOf(number21.getText().toString());
        }

        if(number22.getText().toString().equals("")){
            number2UserArray[1] = -1;
        }else{
            number2UserArray[1] = Integer.valueOf(number22.getText().toString());
        }

        if(number23.getText().toString().equals("")){
            number2UserArray[2] = -1;
        }else{
            number2UserArray[2] = Integer.valueOf(number23.getText().toString());
        }

        if(answer1.getText().toString().equals("")){
            resultUserArray[0] = -1;
        }else{
            resultUserArray[0] = Integer.valueOf(answer1.getText().toString());
        }

        if(answer2.getText().toString().equals("")){
            resultUserArray[1] = -1;
        }else{
            resultUserArray[1] = Integer.valueOf(answer2.getText().toString());
        }

        if(answer3.getText().toString().equals("")){
            resultUserArray[2] = -1;
        }else{
            resultUserArray[2] = Integer.valueOf(answer3.getText().toString());
        }

        if(answer4.getText().toString().equals("")){
            resultUserArray[3] = -1;
        }else{
            resultUserArray[3] = Integer.valueOf(answer4.getText().toString());
        }

        // If a digit is wrong set it to red
        for(int i = 0; i < 4; ++i){
            if(carryUserArray[i] != carryArray[i]){
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        carry1.setBackground(getDrawable(R.drawable.additional_digit_wrong));
                        break;
                    case 1:
                        carry2.setBackground(getDrawable(R.drawable.additional_digit_wrong));
                        break;
                    case 2:
                        carry3.setBackground(getDrawable(R.drawable.additional_digit_wrong));
                        break;
                    case 3:
                        carry4.setBackground(getDrawable(R.drawable.additional_digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        carry1.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                    case 1:
                        carry2.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                    case 2:
                        carry3.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                    case 3:
                        carry4.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                }
            }
        }

        boolean firstWorking = firstWorkingMethod();
        boolean secondWorking = secondWorkingMethod();

        if(wrongCount1 < wrongCount2){
            firstWorkingMethod();
        }else{
            secondWorkingMethod();
        }

        workingCorrectFlag = workingCorrectFlag && (firstWorking || secondWorking);

        for(int i = 0; i < 4; ++i){
            if(resultUserArray[i] != resultArray[i]){
                answerCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        answer1.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        answer2.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        answer3.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 3:
                        answer4.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        answer1.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 1:
                        answer2.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 2:
                        answer3.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 3:
                        answer4.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                }
            }
        }
        if(answerCorrectFlag){
            String message = getString(R.string.correct_answer_message);
            if(workingCorrectFlag) {
                message = getString(R.string.working_correct_message);
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
       /* Button evaluateButton = findViewById(R.id.evaluateAdditionWorkspace);
        evaluateButton.setVisibility(View.GONE);*/
        Button saveAnswer = findViewById(R.id.saveAnswerAddition);
        saveAnswer.setVisibility(View.VISIBLE);
    }

    public boolean firstWorkingMethod(){
        boolean workingCorrectFlag1 = true;
        wrongCount1 = 0;
        for(int i = 0; i < 3; ++i){
            if(number1UserArray[i] != number1Array[i]){
                workingCorrectFlag1 = false;
                switch(i)
                {
                    case 0:
                        number11.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                    case 1:
                        number12.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                    case 2:
                        number13.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        number11.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 1:
                        number12.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 2:
                        number13.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                }
            }
        }

        for(int i = 0; i < 3; ++i){
            if(number2UserArray[i] != number2Array[i]){
                workingCorrectFlag1 = false;
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount1;
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                }
            }
        }
        return workingCorrectFlag1;
    }

    public boolean secondWorkingMethod(){
        boolean workingCorrectFlag2 = true;
        wrongCount2 = 0;
        for(int i = 0; i < 3; ++i){
            if(number1UserArray[i] != number2Array[i]){
                workingCorrectFlag2 = false;
                switch(i)
                {
                    case 0:
                        number11.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                    case 1:
                        number12.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                    case 2:
                        number13.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        number11.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 1:
                        number12.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 2:
                        number13.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                }
            }
        }

        for(int i = 0; i < 3; ++i){
            if(number2UserArray[i] != number1Array[i]){
                workingCorrectFlag2 = false;
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.digit_wrong));
                        ++wrongCount2;
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.question_digit_enable));
                        break;
                }
            }
        }
        return workingCorrectFlag2;
    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    // To check if an answer is entered by the user
    public boolean noAnswerEntered(){
        return answer1.getText().toString().equals("") && answer2.getText().toString().equals("") &&
                answer3.getText().toString().equals("") && answer4.getText().toString().equals("");
    }

    // Save the answer entered by the user
    public void saveAnswer(View view){

        if(!isNetworkConnected()){
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
            return;
        }

        checkBlocks(findViewById(R.id.evaluateAdditionWorkspace));
        int i = 0;
        userAnswer = 0;
        while(resultUserArray[i] == -1){
            i++;
        }
        while(i < 4){
            if(resultUserArray[i] == -1){
                Toast.makeText(this, getString(R.string.invalid_answer_message),
                        Toast.LENGTH_SHORT).show();
                userAnswer = 0;
                return;
            }
            switch(i){
                case 0:
                    userAnswer += resultUserArray[i] * 1000;
                    break;
                case 1:
                    userAnswer += resultUserArray[i] * 100;
                    break;
                case 2:
                    userAnswer += resultUserArray[i] * 10;
                    break;
                case 3:
                    userAnswer += resultUserArray[i];
            }
            i++;
        }
        onBackPressed();
        //Toast.makeText(this, "Answer saved is "+userAnswer+". Press back to continue!", Toast.LENGTH_SHORT).show();
    }
}
