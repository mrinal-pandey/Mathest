package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import oncreate.apps.Mathest.Wrappers.User;


public class StudentHome extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    private final String TAG = "StudentHome";
    TextView nameTxt, classNameTxt, schoolTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);

        nameTxt = findViewById(R.id.name_txt);
        classNameTxt = findViewById(R.id.class_txt);
        schoolTxt = findViewById(R.id.school_txt);

        firebaseFirestore = FirebaseFirestore.getInstance();
        String UID = getIntent().getStringExtra("uid");
        if (!UID.isEmpty()) {
            firebaseFirestore.collection("users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "Doc snapshot: " + documentSnapshot.toString());
                    User m_user = documentSnapshot.toObject(User.class);
                    Log.d(TAG, m_user.getClassName());
                    String name = m_user.getName();
                    String className = m_user.getClassName();
                    String school = m_user.getSchool();

                    nameTxt.setText(name);
                    classNameTxt.setText("Class: " + className);
                    schoolTxt.setText("School: " + school);
                }
            });
        }


    }

    public void launchTest(View view) {
        startActivity(new Intent(this, TestPage.class));
    }
}
