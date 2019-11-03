package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);
    }

    public void launchApp(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}