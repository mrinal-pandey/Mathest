package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

        Intent intent = getIntent();
        number1 = intent.getStringExtra("number1");
        number2 = intent.getStringExtra("number2");
        UID = intent.getStringExtra("UID");

        questionBodyTextView.setText("Add " + number1 + " and " + number2);

        Downloader downloader = new Downloader();
        downloader.execute(this.getString(R.string.mathest_azure_endpoint)+"addition-working?uid="+UID+"&number1="+number1+"&number2="+number2);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(userAnswer != 0){
            TestPage.userAnswer.setText(String.valueOf(userAnswer));
        }
    }

    public void questionEnable(View view){
        view.setBackground(getResources().getDrawable(R.drawable.question_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    public void answerEnable(View view){
        view.setBackground(getResources().getDrawable(R.drawable.answer_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public void repaint(View view){
        Intent intent = new Intent(this, AdditionWorkspace.class);
        intent.putExtra("number1", number1);
        intent.putExtra("number2", number2);
        intent.putExtra("UID", UID);
        finish();
        startActivity(intent);
    }

    public void checkBlocks(View view){

        boolean answerCorrectFlag = true, workingCorrectFlag = true;

        if(noAnswerEntered()){
            Toast.makeText(this, "Please enter an answer!", Toast.LENGTH_SHORT).show();
            return;
        }
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

        /*for(int i = 0; i < 4; ++i){
            System.out.print(carryUserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 3; ++i){
            System.out.print(number1UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 3; ++i){
            System.out.print(number2UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 4; ++i){
            System.out.print(resultUserArray[i] + " ");
        }
        System.out.println();*/

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
            String message = "Congratulations! Your answer is correct!";
            if(workingCorrectFlag) {
                message = "Congratulations! You did it absolutely right!";
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

    public boolean noAnswerEntered(){
        return answer1.getText().toString().equals("") && answer2.getText().toString().equals("") &&
                answer3.getText().toString().equals("") && answer4.getText().toString().equals("");
    }

    public void saveAnswer(View view){
        checkBlocks(findViewById(R.id.evaluateAdditionWorkspace));
        int i = 0;
        userAnswer = 0;
        while(resultUserArray[i] == -1){
            i++;
        }
        while(i < 4){
            if(resultUserArray[i] == -1){
                Toast.makeText(this, "This is not a valid answer! Try again.",
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
