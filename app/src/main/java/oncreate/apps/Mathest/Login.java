package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    FirebaseFirestore firestoreDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        firestoreDatabase = FirebaseFirestore.getInstance();

    }

    public void loginUser(View view) {
        //TODO implement logic to sort out login into two classes, teacher and student.
        startActivity(new Intent(this, StudentHome.class));

    }
}
