package oncreate.apps.Mathest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AdditionWorkspace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition_workspace);
    }

    public void questionEnable(View view){
        view.setBackground(getDrawable(R.drawable.question_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
    public void answerEnable(View view){
        view.setBackground(getDrawable(R.drawable.answer_digit_enable));
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
