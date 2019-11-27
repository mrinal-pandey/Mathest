package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    int[] number1Array = {0, 0, 0};
    int[] number2Array = {0, 0, 0};
    int[] resultArray = {0, 0, 0, 0};
    EditText carry1, carry2, carry3, carry4;
    EditText number11, number12, number13;
    EditText number21, number22, number23;
    EditText answer1, answer2, answer3, answer4;
    int[] carryUserArray = {0, 0, 0, 0};
    int[] number1UserArray = {0, 0, 0};
    int[] number2UserArray = {0, 0, 0};
    int[] resultUserArray = {0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_workspace);

        questionBodyTextView = findViewById(R.id.questionBodyWorkspace);

        Intent intent = getIntent();
        number1 = intent.getStringExtra("number1");
        number2 = intent.getStringExtra("number2");
        UID = intent.getStringExtra("UID");

        questionBodyTextView.setText("Add " + number1 + " and " + number2);

        Downloader downloader = new Downloader();
        downloader.execute("https://mathest.herokuapp.com/addition-working?uid="+UID+"&number1="+number1+"&number2="+number2);

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
            number1UserArray[0] = 0;
        }else{
            number1UserArray[0] = Integer.valueOf(number11.getText().toString());
        }

        if(number12.getText().toString().equals("")){
            number1UserArray[1] = 0;
        }else{
            number1UserArray[1] = Integer.valueOf(number12.getText().toString());
        }

        if(number13.getText().toString().equals("")){
            number1UserArray[2] = 0;
        }else{
            number1UserArray[2] = Integer.valueOf(number13.getText().toString());
        }

        if(number21.getText().toString().equals("")){
            number2UserArray[0] = 0;
        }else{
            number2UserArray[0] = Integer.valueOf(number21.getText().toString());
        }

        if(number22.getText().toString().equals("")){
            number2UserArray[1] = 0;
        }else{
            number2UserArray[1] = Integer.valueOf(number22.getText().toString());
        }

        if(number23.getText().toString().equals("")){
            number2UserArray[2] = 0;
        }else{
            number2UserArray[2] = Integer.valueOf(number23.getText().toString());
        }

        if(answer1.getText().toString().equals("")){
            resultUserArray[0] = 0;
        }else{
            resultUserArray[0] = Integer.valueOf(answer1.getText().toString());
        }

        if(answer2.getText().toString().equals("")){
            resultUserArray[1] = 0;
        }else{
            resultUserArray[1] = Integer.valueOf(answer2.getText().toString());
        }

        if(answer3.getText().toString().equals("")){
            resultUserArray[2] = 0;
        }else{
            resultUserArray[2] = Integer.valueOf(answer3.getText().toString());
        }

        if(answer4.getText().toString().equals("")){
            resultUserArray[3] = 0;
        }else{
            resultUserArray[3] = Integer.valueOf(answer4.getText().toString());
        }

        for(int i = 0; i < 4; ++i){
            if(carryUserArray[i] != carryArray[i]){
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
            }/*else{
                switch(i)
                {
                    case 0:
                        carry1.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                        break;
                    case 1:
                        carry2.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                        break;
                    case 2:
                        carry3.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                        break;
                    case 3:
                        carry4.setBackgroundColor(getResources().getColor(R.color.whiteColor));
                        break;
                }
            }*/
        }

        for(int i = 0; i < 3; ++i){
            if(number1UserArray[i] != number1Array[i]){
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
            }/*else{
                switch(i)
                {
                    case 0:
                        number11.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                    case 1:
                        number12.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                    case 2:
                        number13.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                }
            }*/
        }

        for(int i = 0; i < 3; ++i){
            if(number2UserArray[i] != number2Array[i]){
                switch(i)
                {
                    case 0:
                        number21.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 1:
                        number22.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                    case 2:
                        number23.setBackground(getDrawable(R.drawable.digit_wrong));
                        break;
                }
            }/*else{
                switch(i)
                {
                    case 0:
                        number21.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                    case 1:
                        number22.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                    case 2:
                        number23.setBackgroundColor(R.drawable.question_digit_enable);
                        break;
                }
            }*/
        }

        for(int i = 0; i < 4; ++i){
            if(resultUserArray[i] != resultArray[i]){
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
            }/*else{
                switch(i)
                {
                    case 0:
                        answer1.setBackgroundColor(R.drawable.answer_digit_enable);
                        break;
                    case 1:
                        answer2.setBackgroundColor(R.drawable.answer_digit_enable);
                        break;
                    case 2:
                        answer3.setBackgroundColor(R.drawable.answer_digit_enable);
                        break;
                    case 3:
                        answer4.setBackgroundColor(R.drawable.answer_digit_enable);
                        break;
                }
            }*/
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
        Button evaluateButton = findViewById(R.id.evaluateAdditionWorkspace);
        evaluateButton.setVisibility(View.GONE);
        Button saveAnswer = findViewById(R.id.saveAnswerAddition);
        saveAnswer.setVisibility(View.VISIBLE);
    }
    public boolean noAnswerEntered(){
        if(answer1.getText().toString().equals("") && answer2.getText().toString().equals("") &&
                answer3.getText().toString().equals("") && answer4.getText().toString().equals("")){
            return true;
        }
        return false;
    }
}
