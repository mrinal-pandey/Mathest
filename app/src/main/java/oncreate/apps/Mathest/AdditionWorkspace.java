package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    EditText carry1, carry2, carry3, carry4, number11, number12, number13,
            number21, number22, number23, answer1, answer2, answer3, answer4;

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
        view.setBackground(getDrawable(R.drawable.question_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    public void answerEnable(View view){
        view.setBackground(getDrawable(R.drawable.answer_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public void checkBlocks(View view){
        if(answerNotEntered()){
            Toast.makeText(this, "Please enter an answer!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public boolean answerNotEntered(){
        if(answer1.getText().toString() == "" && answer2.getText().toString() == "" &&
                answer3.getText().toString() == "" && answer4.getText().toString() == ""){
            return true;
        }
        return false;
    }
}
