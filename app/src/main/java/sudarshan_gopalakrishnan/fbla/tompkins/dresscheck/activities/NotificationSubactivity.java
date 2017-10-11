package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.PostFeedAdapter;

/**
 * Created by Sudarshan on 2/1/2016.
 */
public class NotificationSubactivity extends Activity {

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_notificationdisplay);
        ListView listView = (ListView) findViewById(R.id.notificationPostFeed);
        listView.setAdapter(new PostFeedAdapter(this, DressCheckApplication.CMP.getPostNotifications()));

    }

    @Override
    public void onBackPressed() {
        DressCheckApplication.CMP.clearNotifications();
        finish();
    }
}
