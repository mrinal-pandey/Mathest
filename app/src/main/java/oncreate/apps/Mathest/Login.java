package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    final private String TAG = "Login_Mathest";
    FirebaseFirestore firestoreDatabase;
    EditText UIDInput;
    String UID;
    String sheetLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firestoreDatabase = FirebaseFirestore.getInstance();
        UIDInput = findViewById(R.id.UID_input);

    }

    public void loginUser(View view) {
        //TODO implement logic to sort out login into two classes, teacher and student.
        UID = UIDInput.getText().toString();

        if (!UID.isEmpty()) {
            firestoreDatabase.collection(getString(R.string.node_users))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "UID entered: " + UID + " UID from database: " + document.getId());
                                    if (document.getId().equals(UID)) {
                                        try {
                                            sheetLink = document.getData().get("Sheet link").toString();
                                        } catch (NullPointerException e) {
                                            sheetLink = "";
                                        }
                                        Log.d(TAG, "Sheet link: " + sheetLink);

                                        Intent in = new Intent(getApplicationContext(), StudentHome.class);
                                        in.putExtra("link", sheetLink);
                                        in.putExtra("uid", UID);
                                        startActivity(in);
                                        //TODO implement finish after implementing a cached sign-in system.
                                        //finish();
                                        break;
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter a valid UID", Toast.LENGTH_LONG).show();
        }


    }

    public void signupUser(View view) {
        //Screen to sign-up a new user.
        startActivity(new Intent(this, Signup.class));
    }


}
