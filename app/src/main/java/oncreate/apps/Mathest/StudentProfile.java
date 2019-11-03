package oncreate.apps.Mathest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class StudentProfile extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile);

        toolbar = findViewById(R.id.student_profile_toolbar);
        setSupportActionBar(toolbar);
    }

}
