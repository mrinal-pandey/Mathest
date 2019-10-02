package oncreate.apps.Mathest;

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

import oncreate.apps.Mathest.Wrappers.User;

public class Signup extends AppCompatActivity {

    TextView UID_disp;
    EditText nameEdit, schoolEdit, classEdit;
    FirebaseFirestore firestoreDatabase;
    String UID;

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
        classEdit = findViewById(R.id.class_edit);

        UID_disp.setText("UID is: " + UID);

    }

    public void submitDetails(View view) {
        String name = nameEdit.getText().toString();
        String school = schoolEdit.getText().toString();
        String className = classEdit.getText().toString();
        String url = "";

        User m_user = new User(name, url, school, className);

        firestoreDatabase.collection("users").document(UID).set(m_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "User successfully added", Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
}
