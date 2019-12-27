package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultiplicationWorkspace extends AppCompatActivity {

    String number1;
    String number2;
    String UID;
    int[] number1Array = {-1, -1, -1};
    int[] number2Array = {-1, -1, -1};
    int[] carryArray = {0, 0, 0, 0, 0, 0};
    int[] subnum1Array = {-1, -1, -1, -1, -1, -1};
    int[] subnum2Array = {-1, -1, -1, -1, -1, -1};
    int[] subnum3Array = {-1, -1, -1, -1, -1, -1};
    int[] resultArray = {-1, -1, -1, -1, -1, -1};
    TextView questionBodyTextView;
    EditText number11, number12, number13;
    EditText number21, number22, number23;
    EditText carry1, carry2, carry3, carry4, carry5, carry6;
    EditText subnum11, subnum12, subnum13, subnum14, subnum15, subnum16;
    EditText subnum21, subnum22, subnum23, subnum24, subnum25, subnum26;
    EditText subnum31, subnum32, subnum33, subnum34, subnum35, subnum36;
    EditText result1, result2, result3, result4, result5, result6;
    int[] number1UserArray = {-1, -1, -1};
    int[] number2UserArray = {-1, -1, -1};
    int[] carryUserArray = {0, 0, 0, 0, 0, 0};
    int[] subnum1UserArray = {-1, -1, -1, -1, -1, -1};
    int[] subnum2UserArray = {-1, -1, -1, -1, -1, -1};
    int[] subnum3UserArray = {-1, -1, -1, -1, -1, -1};
    int[] resultUserArray = {-1, -1, -1, -1, -1, -1};
    int userAnswer = 0;

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
            //Log.i("JSON", s);
            try{

                JSONArray jsonArray = new JSONArray(s);
                JSONObject number1Details = jsonArray.getJSONObject(1);
                JSONObject number2Details = jsonArray.getJSONObject(2);
                JSONObject carryDetails = jsonArray.getJSONObject(3);
                JSONObject subnum1Details = jsonArray.getJSONObject(4);
                JSONObject subnum2Details = jsonArray.getJSONObject(5);
                JSONObject subnum3Details = jsonArray.getJSONObject(6);
                JSONObject resultDetails = jsonArray.getJSONObject(7);
                for(int i = 0; i < 3; ++i) {
                    number1Array[i] = Integer.valueOf(number1Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 3; ++i) {
                    number2Array[i] = Integer.valueOf(number2Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 6; ++i) {
                    carryArray[i] = Integer.valueOf(carryDetails.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 6; ++i) {
                    subnum1Array[i] = Integer.valueOf(subnum1Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 6; ++i) {
                    subnum2Array[i] = Integer.valueOf(subnum2Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 6; ++i) {
                    subnum3Array[i] = Integer.valueOf(subnum3Details.getString("digit" + (i + 1)));
                }
                for(int i = 0; i < 6; ++i) {
                    resultArray[i] = Integer.valueOf(resultDetails.getString("digit" + (i + 1)));
                }

                /*for(int i = 0; i < 3; ++i){
                    System.out.print(number1Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 3; ++i){
                    System.out.print(number2Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 6; ++i){
                    System.out.print(carryArray[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 6; ++i){
                    System.out.print(subnum1Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 6; ++i){
                    System.out.print(subnum2Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 6; ++i){
                    System.out.print(subnum3Array[i] + " ");
                }
                System.out.println();
                for(int i = 0; i < 6; ++i){
                    System.out.print(resultArray[i] + " ");
                }
                System.out.println();*/

            }catch (Exception e){

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(userAnswer != 0){
            TestPage.userAnswer.setText(String.valueOf(userAnswer));
        }
    }

    public void checkBlocks(View view){

        if(!isNetworkConnected()){
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
            return;
        }

        boolean workingCorrectFlag = true, answerCorrectFlag = true;

        if(noAnswerEntered()){
            Toast.makeText(this, "Please enter an answer!", Toast.LENGTH_SHORT).show();
            return;
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

        if(carry5.getText().toString().equals("")){
            carryUserArray[4] = 0;
        }else {
            carryUserArray[4] = Integer.valueOf(carry5.getText().toString());
        }

        if(carry6.getText().toString().equals("")){
            carryUserArray[5] = 0;
        }else {
            carryUserArray[5] = Integer.valueOf(carry6.getText().toString());
        }

        if(subnum11.getText().toString().equals("")){
            subnum1UserArray[0] = -1;
        }else{
            subnum1UserArray[0] = Integer.valueOf(subnum11.getText().toString());
        }

        if(subnum12.getText().toString().equals("")){
            subnum1UserArray[1] = -1;
        }else{
            subnum1UserArray[1] = Integer.valueOf(subnum12.getText().toString());
        }

        if(subnum13.getText().toString().equals("")){
            subnum1UserArray[2] = -1;
        }else{
            subnum1UserArray[2] = Integer.valueOf(subnum13.getText().toString());
        }

        if(subnum14.getText().toString().equals("")){
            subnum1UserArray[3] = -1;
        }else{
            subnum1UserArray[3] = Integer.valueOf(subnum14.getText().toString());
        }

        if(subnum15.getText().toString().equals("")){
            subnum1UserArray[4] = -1;
        }else{
            subnum1UserArray[4] = Integer.valueOf(subnum15.getText().toString());
        }

        if(subnum16.getText().toString().equals("")){
            subnum1UserArray[5] = -1;
        }else{
            subnum1UserArray[5] = Integer.valueOf(subnum16.getText().toString());
        }

        if(subnum21.getText().toString().equals("")){
            subnum2UserArray[0] = -1;
        }else{
            subnum2UserArray[0] = Integer.valueOf(subnum21.getText().toString());
        }

        if(subnum22.getText().toString().equals("")){
            subnum2UserArray[1] = -1;
        }else{
            subnum2UserArray[1] = Integer.valueOf(subnum22.getText().toString());
        }

        if(subnum23.getText().toString().equals("")){
            subnum2UserArray[2] = -1;
        }else{
            subnum2UserArray[2] = Integer.valueOf(subnum23.getText().toString());
        }

        if(subnum24.getText().toString().equals("")){
            subnum2UserArray[3] = -1;
        }else{
            subnum2UserArray[3] = Integer.valueOf(subnum24.getText().toString());
        }

        if(subnum25.getText().toString().equals("")){
            subnum2UserArray[4] = -1;
        }else{
            subnum2UserArray[4] = Integer.valueOf(subnum25.getText().toString());
        }

        if(subnum26.getText().toString().equals("")){
            subnum2UserArray[5] = -1;
        }else{
            subnum2UserArray[5] = Integer.valueOf(subnum26.getText().toString());
        }

        if(subnum31.getText().toString().equals("")){
            subnum3UserArray[0] = -1;
        }else{
            subnum3UserArray[0] = Integer.valueOf(subnum31.getText().toString());
        }

        if(subnum32.getText().toString().equals("")){
            subnum3UserArray[1] = -1;
        }else{
            subnum3UserArray[1] = Integer.valueOf(subnum32.getText().toString());
        }

        if(subnum33.getText().toString().equals("")){
            subnum3UserArray[2] = -1;
        }else{
            subnum3UserArray[2] = Integer.valueOf(subnum33.getText().toString());
        }

        if(subnum34.getText().toString().equals("")){
            subnum3UserArray[3] = -1;
        }else{
            subnum3UserArray[3] = Integer.valueOf(subnum34.getText().toString());
        }

        if(subnum35.getText().toString().equals("")){
            subnum3UserArray[4] = -1;
        }else{
            subnum3UserArray[4] = Integer.valueOf(subnum35.getText().toString());
        }

        if(subnum36.getText().toString().equals("")){
            subnum3UserArray[5] = -1;
        }else{
            subnum3UserArray[5] = Integer.valueOf(subnum36.getText().toString());
        }

        if(result1.getText().toString().equals("")){
            resultUserArray[0] = -1;
        }else{
            resultUserArray[0] = Integer.valueOf(result1.getText().toString());
        }

        if(result2.getText().toString().equals("")){
            resultUserArray[1] = -1;
        }else{
            resultUserArray[1] = Integer.valueOf(result2.getText().toString());
        }

        if(result3.getText().toString().equals("")){
            resultUserArray[2] = -1;
        }else{
            resultUserArray[2] = Integer.valueOf(result3.getText().toString());
        }

        if(result4.getText().toString().equals("")){
            resultUserArray[3] = -1;
        }else{
            resultUserArray[3] = Integer.valueOf(result4.getText().toString());
        }

        if(result5.getText().toString().equals("")){
            resultUserArray[4] = -1;
        }else{
            resultUserArray[4] = Integer.valueOf(result5.getText().toString());
        }

        if(result6.getText().toString().equals("")){
            resultUserArray[5] = -1;
        }else{
            resultUserArray[5] = Integer.valueOf(result6.getText().toString());
        }

        /*for(int i = 0; i < 3; ++i){
            System.out.print(number1UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 3; ++i){
            System.out.print(number2UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; ++i){
            System.out.print(carryUserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; ++i){
            System.out.print(subnum1UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; ++i){
            System.out.print(subnum2UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; ++i){
            System.out.print(subnum3UserArray[i] + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; ++i){
            System.out.print(resultUserArray[i] + " ");
        }
        System.out.println();*/

        for(int i = 0; i < 3; ++i){
            if(number1UserArray[i] != number1Array[i]){
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        number11.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        number12.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        number13.setBackground(getDrawable(R.drawable.digit_wrong));
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
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.digit_wrong));                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.digit_wrong));
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

        for(int i = 0; i < 6; ++i){
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
                    case 4:
                        carry5.setBackground(getDrawable(R.drawable.additional_digit_wrong));
                        break;
                    case 5:
                        carry6.setBackground(getDrawable(R.drawable.additional_digit_wrong));
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
                    case 4:
                        carry5.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                    case 5:
                        carry6.setBackground(getDrawable(R.drawable.additional_digit_disable));
                        break;
                }
            }
        }

        for(int i = 0; i < 6; ++i){
            if(subnum1UserArray[i] != subnum1Array[i]){
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        subnum11.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        subnum12.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        subnum13.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 3:
                        subnum14.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 4:
                        subnum15.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 5:
                        subnum16.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        subnum11.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 1:
                        subnum12.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 2:
                        subnum13.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 3:
                        subnum14.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 4:
                        subnum15.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 5:
                        subnum16.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                }
            }
        }

        for(int i = 0; i < 6; ++i){
            if(subnum2UserArray[i] != subnum2Array[i]){
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        subnum21.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        subnum22.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        subnum23.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 3:
                        subnum24.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 4:
                        subnum25.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 5:
                        subnum26.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        subnum21.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 1:
                        subnum22.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 2:
                        subnum23.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 3:
                        subnum24.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 4:
                        subnum25.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 5:
                        subnum26.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                }
            }
        }

        for(int i = 0; i < 6; ++i){
            if(subnum3UserArray[i] != subnum3Array[i]){
                workingCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        subnum31.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        subnum32.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        subnum33.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 3:
                        subnum34.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 4:
                        subnum35.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 5:
                        subnum36.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        subnum31.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 1:
                        subnum32.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 2:
                        subnum33.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 3:
                        subnum34.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 4:
                        subnum35.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 5:
                        subnum36.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                }
            }
        }

        for(int i = 0; i < 6; ++i){
            if(resultUserArray[i] != resultArray[i]){
                answerCorrectFlag = false;
                switch(i)
                {
                    case 0:
                        result1.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        result2.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        result3.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 3:
                        result4.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 4:
                        result5.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 5:
                        result6.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }else{
                switch(i)
                {
                    case 0:
                        result1.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 1:
                        result2.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 2:
                        result3.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 3:
                        result4.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 4:
                        result5.setBackground(getDrawable(R.drawable.answer_digit_enable));
                        break;
                    case 5:
                        result6.setBackground(getDrawable(R.drawable.answer_digit_enable));
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

        Button saveAnswer = findViewById(R.id.saveAnswerMultiplication);
        saveAnswer.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean noAnswerEntered(){
        return result1.getText().toString().equals("") && result2.getText().toString().equals("") &&
                result3.getText().toString().equals("") && result4.getText().toString().equals("") &&
                result5.getText().toString().equals("") && result6.getText().toString().equals("");
    }

    public void saveAnswer(View view){

        if(!isNetworkConnected()){
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
            return;
        }

        checkBlocks(findViewById(R.id.evaluateMultiplicationWorkspace));
        int i = 0;
        userAnswer = 0;
        while(resultUserArray[i] == -1){
            i++;
        }
        while(i < 6){
            if(resultUserArray[i] == -1){
                Toast.makeText(this, "This is not a valid answer! Try again.",
                        Toast.LENGTH_SHORT).show();
                userAnswer = 0;
                return;
            }
            switch(i){
                case 0:
                    userAnswer += resultUserArray[i] * 100000;
                    break;
                case 1:
                    userAnswer += resultUserArray[i] * 10000;
                    break;
                case 2:
                    userAnswer += resultUserArray[i] * 1000;
                    break;
                case 3:
                    userAnswer += resultUserArray[i] * 100;
                    break;
                case 4:
                    userAnswer += resultUserArray[i] * 10;
                    break;
                case 5:
                    userAnswer += resultUserArray[i];
                    break;
            }
            i++;
        }
        onBackPressed();
        //Toast.makeText(this, "Answer saved is "+userAnswer+". Press back to continue!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_workspace);

        questionBodyTextView = findViewById(R.id.questionBodyMultiplicationWorkspace);

        Intent intent = getIntent();
        number1 = intent.getStringExtra("number1");
        number2 = intent.getStringExtra("number2");
        UID = intent.getStringExtra("UID");

        questionBodyTextView.setText("Multiply " + number1 + " and " + number2);

        if(isNetworkConnected()) {
            Downloader downloader = new Downloader();
            downloader.execute(this.getString(R.string.mathest_azure_endpoint) + "multiplication-working?uid=" + UID + "&number1=" + number1 + "&number2=" + number2);
        }else{
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
        }

        number11 = findViewById(R.id.number1_digit1);
        number12 = findViewById(R.id.number1_digit2);
        number13 = findViewById(R.id.number1_digit3);
        number21 = findViewById(R.id.number2_digit1);
        number22 = findViewById(R.id.number2_digit2);
        number23 = findViewById(R.id.number2_digit3);
        carry1 = findViewById(R.id.carry_digit_1);
        carry2 = findViewById(R.id.carry_digit_2);
        carry3 = findViewById(R.id.carry_digit_3);
        carry4 = findViewById(R.id.carry_digit_4);
        carry5 = findViewById(R.id.carry_digit_5);
        carry6 = findViewById(R.id.carry_digit_6);
        subnum11 = findViewById(R.id.line1_digit1);
        subnum12 = findViewById(R.id.line1_digit2);
        subnum13 = findViewById(R.id.line1_digit3);
        subnum14 = findViewById(R.id.line1_digit4);
        subnum15 = findViewById(R.id.line1_digit5);
        subnum16 = findViewById(R.id.line1_digit6);
        subnum21 = findViewById(R.id.line2_digit1);
        subnum22 = findViewById(R.id.line2_digit2);
        subnum23 = findViewById(R.id.line2_digit3);
        subnum24 = findViewById(R.id.line2_digit4);
        subnum25 = findViewById(R.id.line2_digit5);
        subnum26 = findViewById(R.id.line2_digit6);
        subnum31 = findViewById(R.id.line3_digit1);
        subnum32 = findViewById(R.id.line3_digit2);
        subnum33 = findViewById(R.id.line3_digit3);
        subnum34 = findViewById(R.id.line3_digit4);
        subnum35 = findViewById(R.id.line3_digit5);
        subnum36 = findViewById(R.id.line3_digit6);
        result1 = findViewById(R.id.result_digit1);
        result2 = findViewById(R.id.result_digit2);
        result3 = findViewById(R.id.result_digit3);
        result4 = findViewById(R.id.result_digit4);
        result5 = findViewById(R.id.result_digit5);
        result6 = findViewById(R.id.result_digit6);

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
        Intent intent = new Intent(this, MultiplicationWorkspace.class);
        intent.putExtra("number1", number1);
        intent.putExtra("number2", number2);
        intent.putExtra("UID", UID);
        finish();
        startActivity(intent);
    }
}
