package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.drm.DrmErrorEvent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Direction;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.LargePostDisplayActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SwipeListener;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.PastPostsAdapter;


public class OnlyClientPostDisplayActivity extends Activity implements LargePostDisplayActivity{

    private Spinner postSpinner;
    private ListView listView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_post_vertical);


        postSpinner = (Spinner)findViewById(R.id.postSelector);
        ArrayAdapter<Post> adapter = new ArrayAdapter<Post>(this,R.layout.layout_customspinner, DressCheckApplication.CMP.requestMyPosts()){
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(25);
                ((TextView) v).setTextColor(Color.WHITE);

                return v;
            }
        };
        postSpinner.setAdapter(adapter);
        postSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listView.setAdapter(new PastPostsAdapter(OnlyClientPostDisplayActivity.this, (Post) postSpinner.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new PastPostsAdapter(this, (Post) postSpinner.getSelectedItem()));
        listView.setOnTouchListener(new View.OnTouchListener() {
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
        gestureDetector = new GestureDetector(new SwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if (direction.equals(Direction.left)) {
                    Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                    i.putExtra("StringMessage", "Updating Posts");
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

        if(DressCheckApplication.tutorialCount_myposts > 0) {
            createDialogAlert();
            DressCheckApplication.tutorialCount_myposts--;
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
        postSpinner.setAdapter(null);
        postSpinner = null;
        super.onDestroy();
        System.gc();
    }

    @Override
    public void finish(){
        DressCheckApplication.CMP.clearMyPosts();
        super.finish();
    }

    @Override
    public void updateAdapter() {
        listView.setAdapter(new PastPostsAdapter(this, (Post) postSpinner.getSelectedItem()));
    }

    private void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(OnlyClientPostDisplayActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_mypostinstruction, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnlyClientPostDisplayActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }


}
