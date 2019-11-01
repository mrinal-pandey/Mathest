package oncreate.apps.Mathest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

import java.util.Random;

import oncreate.apps.Mathest.UI.DialogHandler;
import oncreate.apps.Mathest.Wrappers.User;

public class Signup extends AppCompatActivity {

    TextView UID_disp;
    EditText nameEdit, schoolEdit, gradeEdit;
    FirebaseFirestore firestoreDatabase;
    String UID;
    boolean userAdded = false;
    DialogHandler dialogHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firestoreDatabase = FirebaseFirestore.getInstance();
        Random r = new Random();
        UID = String.valueOf(r.nextInt(999));

        UID_disp = findViewById(R.id.UID_disp);
        nameEdit = findViewById(R.id.name_edit);
        schoolEdit = findViewById(R.id.school_edit);
        gradeEdit = findViewById(R.id.grade_edit);
        dialogHandler = new DialogHandler(this);

        UID_disp.setText("Your UID is: " + UID);

    }

    public void submitDetails(View view) {
        if(isNetworkConnected()) {
            dialogHandler.showDialog();

            String name = nameEdit.getText().toString();
            String school = schoolEdit.getText().toString();
            int grade = Integer.parseInt(gradeEdit.getText().toString());
            String url = "";

            User m_user = new User(name, url, school, grade);

            firestoreDatabase.collection("users").document(UID).set(m_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    userAdded = true;
                    dialogHandler.hideDialog();
                    Toast.makeText(getApplicationContext(), "User successfully added", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            if (userAdded) {
                Toast.makeText(this, "Unable to add user, please try again..", Toast.LENGTH_LONG).show();
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
