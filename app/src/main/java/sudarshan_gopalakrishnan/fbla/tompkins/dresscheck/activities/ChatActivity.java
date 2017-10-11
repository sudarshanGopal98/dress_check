package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.ChatAdapter;


public class ChatActivity extends Activity {
    private ListView chatView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_chat);

        Button help = (Button) findViewById(R.id.userHelper2);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogAlert();
            }
        });

        chatView = (ListView) findViewById(R.id.chatItemList);
        chatView.setAdapter(new ChatAdapter(this));

        if(DressCheckApplication.tutorialCount_chat > 0) {
            createDialogAlert();
            DressCheckApplication.tutorialCount_chat--;
        }


    }

    public void setAdapter(ChatAdapter adapter) {
        chatView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        ChatAdapter currentAdapter = (ChatAdapter) chatView.getAdapter();
        if (currentAdapter.getCount() - 1 != DressCheckApplication.CMP.getUsers().size()) {
            chatView.setAdapter(new ChatAdapter(this));
            return;
        }

        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(ChatActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_chat, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
