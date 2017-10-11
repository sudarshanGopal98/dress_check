package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;


public class TutorialActivity extends Activity{
    private ListView tutorialGallery;
    private Button endTutorial;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tutorial);

        endTutorial = (Button) findViewById(R.id.endTutorial);
        endTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File tutorialCheck = new File(getFilesDir(), "TutorialCheck");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tutorialCheck));
                    bw.write("noCheck");
                    bw.flush();
                    bw.close();
                }catch(Exception e){}

                if(DressCheckApplication.CMP.getClient() == null){
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(i);
                    finish();
                }
            }
        });

    }


}
