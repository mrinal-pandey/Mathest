package oncreate.apps.Mathest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import oncreate.apps.Mathest.UI.DialogHandler;
import oncreate.apps.Mathest.Wrappers.User;

public class Signup extends AppCompatActivity {

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

            try {
                Log.i("JSON content", s);
                JSONArray jsonArray = new JSONArray(s);

                for(int i = 0; i < jsonArray.length(); ++i) {

                    JSONObject details = jsonArray.getJSONObject(i);
                    sheetID = details.getString("id");
                    Log.i("SheetID", sheetID);
                }
            }
            catch (Exception e)
            {

            }

            User m_user = new User(name, sheetID, school, grade, UID);

            firestoreDatabase.collection("users").document(UID).set(m_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    userAdded = true;
                    dialogHandler.hideDialog();
                    Toast.makeText(getApplicationContext(), "User successfully added", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        }
    }

    TextView UID_disp;
    EditText nameEdit, schoolEdit, gradeEdit;
    FirebaseFirestore firestoreDatabase;
    String UID;
    String sheetID, name, school;
    int grade;
    boolean userAdded = false;
    DialogHandler dialogHandler;
    List<String> listOfUIDs;

    public void UIDValidator(final String UIDGenerated){
        listOfUIDs = new ArrayList<>();
        firestoreDatabase.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listOfUIDs.add(document.getId());
                    }
                    for(String uid : listOfUIDs){
                        if(uid.equals(UIDGenerated)){
                            UIDGenerator();
                        }
                    }
                    UID_disp.setText("Your UID is: " + UID);
                }
            }
        });
    }

    public void UIDGenerator(){
        UID = String.valueOf((int)((Math.random() * 999) + 1));
        UIDValidator(UID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firestoreDatabase = FirebaseFirestore.getInstance();

        UID_disp = findViewById(R.id.UID_disp);
        nameEdit = findViewById(R.id.name_edit);
        schoolEdit = findViewById(R.id.school_edit);
        gradeEdit = findViewById(R.id.grade_edit);
        dialogHandler = new DialogHandler(this);

        UIDGenerator();
    }

    boolean nameEntered = false;

    public void submitDetails(View view) {
        if(isNetworkConnected()) {

            if(nameEdit.getText().toString().equals("")) {
                nameEdit.setHintTextColor(getResources().getColor(R.color.wrongAnswerColor));
                nameEdit.setHint("Please provide Full Name");
            }else{
                nameEntered = true;
                name = nameEdit.getText().toString();
            }
            if(schoolEdit.getText().toString().equals("")){
                school = "NA";
            }else {
                school = schoolEdit.getText().toString();
            }
            if(gradeEdit.getText().toString().equals("")){
                grade = -1;
            }else {
                grade = Integer.parseInt(gradeEdit.getText().toString());
            }

            if(nameEntered) {

                dialogHandler.showDialog();

                Downloader task = new Downloader();
                task.execute(this.getString(R.string.mathest_azure_endpoint) + "sheet?uid=" + UID);

                if (userAdded) {
                    Toast.makeText(this, "Unable to add user, please try again..", Toast.LENGTH_LONG).show();
                }
            }

        }else{
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
