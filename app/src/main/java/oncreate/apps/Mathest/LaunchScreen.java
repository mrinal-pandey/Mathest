package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

public class LaunchScreen extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        imageView = findViewById(R.id.logoImageView);
        imageView.animate().rotationY(360).setDuration(1000);

        CountDownTimer countDownTimer=  new CountDownTimer(1000,1000){

            public void onTick(long millisecondsUntilDone){

            }

            public void onFinish(){

                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }

        };
        countDownTimer.start();

    }
}