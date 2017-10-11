package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Direction;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SwipeListener;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.PostFeedAdapter;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.PostFeedSplash;

public class AllPostFeedActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_postfeed);

        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(new PostFeedAdapter(this, DressCheckApplication.CMP.getAllPosts()));
        lv.setVerticalScrollBarEnabled(false);
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (event != null)
                        return gestureDetector.onTouchEvent(event);
                } catch (Exception e) {
                }
                return false;
            }
        });


        final Switch switchButton = (Switch) findViewById(R.id.switch1);
        switchButton.setChecked(DressCheckApplication.CMP.isAttireFilter());
        switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DressCheckApplication.CMP.setIsAttireFilter(switchButton.isChecked());
                MyLog.print(DressCheckApplication.CMP.isAttireFilter());

                Intent i = new Intent(getApplicationContext(), PostFeedSplash.class);
                i.putExtra("StringMessage", "Updating Posts");
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        gestureDetector = new GestureDetector(new SwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if (direction.equals(Direction.right)) {
                    Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.open_translate_xaxis_lr, R.anim.close_scale);
                    finish();
                }

                if (direction.equals(Direction.up) || direction.equals(Direction.down))
                    return false;
                return true;
            }
        });

        Button needHelp = (Button)findViewById(R.id.userHelper3);
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogAlert();
            }
        });

        if(DressCheckApplication.tutorialCount_postfeed > 0) {
            createDialogAlert();
            DressCheckApplication.tutorialCount_postfeed--;
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent e){
        gestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        System.gc();
    }


    @Override
    public void finish(){
        DressCheckApplication.CMP.clearAllPosts();
        super.finish();
    }

    private void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(AllPostFeedActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_allpostinstruction, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AllPostFeedActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }
}