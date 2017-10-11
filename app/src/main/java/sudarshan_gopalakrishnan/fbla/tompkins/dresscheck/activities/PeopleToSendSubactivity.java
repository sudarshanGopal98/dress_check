package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.UserSelectAdapter;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.SendingPictureActivitySplash;

/**
 * Created by Sudarshan on 2/1/2016.
 */
public class PeopleToSendSubactivity extends Activity {

    ArrayList<String> peopleToSend;


    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        peopleToSend = new ArrayList<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_select_friends);

        final String messageToPost = getIntent().getSerializableExtra("messageToPost").toString();
        final boolean isAttire = getIntent().getBooleanExtra("isAttire", true);

        ListView listView = (ListView) findViewById(R.id.friendNames);
        listView.setAdapter(new UserSelectAdapter(this));

        final CheckBox isPostFacebook = (CheckBox) findViewById(R.id.postFacebook);

        Button postButton = (Button) findViewById(R.id.postButton3);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPostFacebook.isChecked()){
                    Intent i = new Intent(getApplicationContext(), FacebookPostActivity.class);
                    i.putExtra("messageToPost", messageToPost);
                    i.putExtra("isAttire", isAttire);
                    i.putExtra("peopleToSend", peopleToSend.toString());
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }else{
                    Intent i = new Intent(getApplicationContext(), SendingPictureActivitySplash.class);
                    i.putExtra("StringMessage", "Uploading Your Picture");
                    i.putExtra("messageToPost", messageToPost);
                    i.putExtra("isAttire", isAttire);
                    i.putExtra("peopleToSend", peopleToSend.toString());
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        });


    }
    public void addPerson(String s){
        peopleToSend.add(s);
    }

    public void removePerson(String s){
        peopleToSend.remove(s);
    }

}
