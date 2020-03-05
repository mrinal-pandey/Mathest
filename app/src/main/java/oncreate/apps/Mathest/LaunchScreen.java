/*
 This screen is the splash screen of the application
*/

package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

public class LaunchScreen extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        // To avoid glitches in the interface, restricting the screen orientation to portrait
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // setting animation to the logo
        imageView = findViewById(R.id.logoImageView);
        imageView.animate().rotationY(360).setDuration(1000);

        // Timer to create a delay so that animation can be visualized
        CountDownTimer countDownTimer=  new CountDownTimer(1000,1000){

            public void onTick(long millisecondsUntilDone){

            }

            // Intent to Login screen after the timer expires
            public void onFinish(){

                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }

        };
        countDownTimer.start();

    }
}