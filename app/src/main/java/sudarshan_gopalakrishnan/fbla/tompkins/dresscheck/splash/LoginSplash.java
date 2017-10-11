package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.LoginProcess;


public class LoginSplash extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyLog.print("Entered Created");

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_logincheck);
        new LoginProcess(this).execute();



    }


}