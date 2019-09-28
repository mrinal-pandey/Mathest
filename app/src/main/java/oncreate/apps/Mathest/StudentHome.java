package oncreate.apps.Mathest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class StudentHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);
    }

    public void launchTest(View view) {
        startActivity(new Intent(this, TestPage.class));
    }
}