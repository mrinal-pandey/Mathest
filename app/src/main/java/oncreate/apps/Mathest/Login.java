/*
 Login screen of the application
*/

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

    final private String TAG = "Login_Mathest"; // For debugging purpose
    FirebaseFirestore firestoreDatabase; // Firebase cloud instance
    EditText UIDInput; // To target UID edit text
    String UID; // To store UID of the user
    DialogHandler dialogHandler; // To handle the loading dialog
    SharedPreferences sharedPreferences; // Shared preference instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // To avoid glitches in the interface, restricting the screen orientation to portrait
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setting up shared preferences
        sharedPreferences = this.getSharedPreferences("usercontent", Context.MODE_PRIVATE);

        // Setting up firebase cloud instance
        firestoreDatabase = FirebaseFirestore.getInstance();
        // Finding UID input edit text by id
        UIDInput = findViewById(R.id.UID_input);

        // Setting up dialog handler
        dialogHandler = new DialogHandler(this);

        // Getting UID from local storage
        String UID = sharedPreferences.getString("UID", "NA");
        dialogHandler.showDialog(); // Show the dialog
        // If UID is not found locally, just hide the dialog else redirect to Home screen
        if (UID.contentEquals("NA")) {
            dialogHandler.hideDialog();
        } else {
            dialogHandler.hideDialog();
            Intent in = new Intent(getApplicationContext(), StudentHome.class);
            in.putExtra("uid", UID);
            startActivity(in);
        }

    }

    // Method to login a user
    public void loginUser(View view) {
        //TODO implement logic to sort out login into two classes, teacher and student.
        UID = UIDInput.getText().toString();

        // If a UID was provided, proceed
        if (!UID.isEmpty()) {
            // If network is available, proceed
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
                Toast.makeText(this,getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.invalid_uid_message), Toast.LENGTH_LONG).show();
        }


    }

    public void signupUser(View view) {
        //Screen to sign-up a new user.
        if(isNetworkConnected()) {
            startActivity(new Intent(this, Signup.class));
        }else {
            Toast.makeText(this, getString(R.string.no_internet_message), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() {

        //Checks for network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
