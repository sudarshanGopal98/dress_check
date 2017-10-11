package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.AllPostFeedActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SplashActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.update.PostFeedUpdate;


public class PostFeedSplash extends Activity implements SplashActivity {

    private TextView descriptionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_process_waiting);

        descriptionHolder = (TextView) findViewById(R.id.descriptionHolder);
        descriptionHolder.setText(getIntent().getSerializableExtra("StringMessage").toString());


    }

    @Override
    protected void onStart(){
        super.onStart();
        new PostFeedUpdate().execute(this);
    }

    @Override
    public void updateCompleted() {

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DressCheckApplication.CMP.getAllPosts() != null && DressCheckApplication.CMP.getAllPosts().isEmpty()) {
                    createDialogAlert();

                    return;
                }
                Intent mainIntent = new Intent(PostFeedSplash.this, AllPostFeedActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, 1000);

    }

    @Override
    public void updateCompleted(Object resultObject) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(PostFeedSplash.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_nopost, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PostFeedSplash.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent mainIntent = new Intent(PostFeedSplash.this, WelcomeActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    PostFeedSplash.this.finish();
                }
                return true;
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}



