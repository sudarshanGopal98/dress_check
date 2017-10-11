package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.ChatActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SplashActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.update.ConversationUpdate;


public class ConversationSplash extends Activity implements SplashActivity {

    private TextView descriptionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_process_waiting);

        descriptionHolder = (TextView) findViewById(R.id.descriptionHolder);
        descriptionHolder.setText("Updating Conversations");


    }

    @Override
    protected void onStart(){
        super.onStart();
        new ConversationUpdate().execute(this);
    }

    @Override
    public void updateCompleted() {

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(ConversationSplash.this, ChatActivity.class);
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
}



