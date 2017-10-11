package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.ConversationSplash;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.OnlyPostActivitySplash;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.PostFeedSplash;


public class WelcomeActivity extends Activity {
    private TextView welcomeText;
    private TextView progressBarHelper;
    private String username;
    private ProgressBar progressBar;
    private boolean touchedLogOutButton;
    private ImageView conversationButton, postFeedButton, pastPostButton, cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_welcome);

        username = DressCheckApplication.CMP.getClient().getUsername();

        welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText("\n\nWelcome " + username+"\n\n");


        progressBarHelper = (TextView) findViewById(R.id.progressBarHelper);
        progressBarHelper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return progressBarHelper();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.slideBar);
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return progressBarHelper();
            }
        });

        pastPostButton = (ImageView) findViewById(R.id.pastPostButton);
        pastPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OnlyPostActivitySplash.class);
                i.putExtra("StringMessage", "Updating Posts");
                startActivity(i);
                overridePendingTransition(R.anim.open_translate_yaxis_ud, R.anim.close_scale);
                finish();
            }
        });

        postFeedButton = (ImageView) findViewById(R.id.postFeedButton);
        postFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PostFeedSplash.class);
                i.putExtra("StringMessage", "Updating Posts");
                startActivity(i);
                overridePendingTransition(R.anim.open_translate_yaxis_ud, R.anim.close_scale);
                finish();
            }
        });

        conversationButton = (ImageView) findViewById(R.id.conversationButton);
        conversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ConversationSplash.class);
                startActivity(i);
                overridePendingTransition(R.anim.open_translate_yaxis_ud, R.anim.close_scale);
                finish();
            }
        });

        cameraButton = (ImageView) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.open_translate_yaxis_ud, R.anim.close_scale);
                finish();
            }
        });



        if(DressCheckApplication.CMP.getClient().hasPostNotifications()) {
            Intent i = new Intent(getApplicationContext(), NotificationSubactivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }
    private boolean progressBarHelper(){
        touchedLogOutButton = !touchedLogOutButton;

        if (!touchedLogOutButton) {
            progressBarHelper.setText("Tap The Bar Below To Log Out!");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (touchedLogOutButton) {
                    if (progressBar.getProgress() == progressBar.getMax()) {
                        touchedLogOutButton = false;

                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                        if(DressCheckApplication.CMP.logoutProcess(WelcomeActivity.this)) {
                            DressCheckApplication.resetTutorialCount();
                            finish();
                        }
                    }

                    try {
                        progressBarHelper.setText("Logging Out... Tap the Bar Again To Pause Logout");
                        progressBar.setProgress(progressBar.getProgress() + 5);
                        Thread.sleep(100);
                    } catch (Exception e) {}
                }
            }
        };
        new Thread(runnable).start();
        return false;
    }


    @Override
    public void finish(){
        try {
            touchedLogOutButton = false;
            progressBarHelper.setText("Tap The Bar Below To Log Out!");
        }catch (Exception e){}


        super.finish();
    }

}

