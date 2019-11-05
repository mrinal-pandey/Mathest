package oncreate.apps.Mathest.UI;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import oncreate.apps.Mathest.R;

public class CorrectAnswerDialogHandler {

    Activity activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public CorrectAnswerDialogHandler(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.login_dialog);

        //...initialize the imageView form inflated layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        //...now load that gif which we put inside the drawable folder here with the help of Glide

        Glide.with(activity)
                .load(R.drawable.correct_answer)
                .placeholder(R.drawable.correct_answer)
                .centerCrop()
                .into(gifImageView);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog() {
        dialog.dismiss();
    }
}
