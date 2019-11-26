package oncreate.apps.Mathest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
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

import oncreate.apps.Mathest.UI.DialogHandler;

public class Login extends AppCompatActivity {

    final private String TAG = "Login_Mathest";
    FirebaseFirestore firestoreDatabase;
    EditText UIDInput;
    String UID;
    DialogHandler dialogHandler;
    String sheetLink;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = this.getSharedPreferences("usercontent", Context.MODE_PRIVATE);

        firestoreDatabase = FirebaseFirestore.getInstance();
        UIDInput = findViewById(R.id.UID_input);

        dialogHandler = new DialogHandler(this);

        String UID = sharedPreferences.getString("UID", "NA");
        dialogHandler.showDialog();
        if (UID.contentEquals("NA")) {
            dialogHandler.hideDialog();
        } else {
            dialogHandler.hideDialog();
            Intent in = new Intent(getApplicationContext(), StudentHome.class);
            in.putExtra("uid", UID);
            startActivity(in);
        }

    }

    public void loginUser(View view) {
        //TODO implement logic to sort out login into two classes, teacher and student.
        UID = UIDInput.getText().toString();

        if (!UID.isEmpty()) {
            if (isNetworkConnected()) {
                dialogHandler.showDialog();
                firestoreDatabase.collection(getString(R.string.node_users))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean uidNotFound = true;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, "UID entered: " + UID + " UID from database: " + document.getId());
                                        if (document.getId().equals(UID)) {
                                            uidNotFound = false;
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("UID", UID);
                                            editor.apply();

                                            dialogHandler.hideDialog();

                                            Intent in = new Intent(getApplicationContext(), StudentHome.class);

                                            in.putExtra("uid", UID);
                                            startActivity(in);
                                            //TODO implement finish after implementing a cached sign-in system.
                                            //finish();
                                            break;
                                        }
                                    }
                                    if (uidNotFound) {
                                        Log.w(TAG, "UID not found: " + UID);
                                        Toast.makeText(getApplicationContext(), "Invalid UID , please try again", Toast.LENGTH_LONG).show();
                                        dialogHandler.hideDialog();
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                    dialogHandler.hideDialog();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid UID", Toast.LENGTH_LONG).show();
        }


    }

    public void signupUser(View view) {
        //Screen to sign-up a new user.
        if(isNetworkConnected()) {
            startActivity(new Intent(this, Signup.class));
        }else {
            Toast.makeText(this, "No internet detected", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
