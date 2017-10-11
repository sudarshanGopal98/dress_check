package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.AllPostFeedActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SplashActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.NotificationSender;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.update.PictureUpdate;


public class SendingPictureActivitySplash extends Activity implements SplashActivity {

    private TextView descriptionHolder;
    private Post myPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_process_waiting);

        descriptionHolder = (TextView) findViewById(R.id.descriptionHolder);
        descriptionHolder.setText(getIntent().getSerializableExtra("StringMessage").toString());


        myPost = new Post();
        myPost.setPostID(DressCheckApplication.CMP.getClient().nextAvailPicNumber() + "");
        myPost.setImage(DressCheckApplication.CMP.getRecentImage());
        myPost.setMessage(getIntent().getSerializableExtra("messageToPost").toString());
        myPost.setUserWhoPosted(DressCheckApplication.CMP.getClient().getUsername());
        myPost.setIsAttire(Boolean.parseBoolean(getIntent().getSerializableExtra("isAttire").toString()));
        ArrayList<String> listToAdd = new ArrayList<String>();
        myPost.setComments(listToAdd.toString());

        new NotificationSender(getIntent().getSerializableExtra("peopleToSend").toString(), myPost.getPostID());

    }

    @Override
    protected void onStart(){
        super.onStart();
        new PictureUpdate(this).execute(myPost);

    }

    @Override
    public void updateCompleted() {
        Intent i = new Intent(getApplicationContext(),WelcomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
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



