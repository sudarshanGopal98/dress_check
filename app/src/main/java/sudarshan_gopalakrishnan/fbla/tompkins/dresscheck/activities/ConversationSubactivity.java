package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Conversation;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.ChatAdapter;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.ConversationAdapter;

/**
 * Created by Sudarshan on 1/29/2016.
 */
public class ConversationSubactivity extends Activity {

    ListView chatMessages;
    EditText messageText;
    Button messageSend;
    Conversation c;
    Runnable updateManager;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        int conversationID = Integer.parseInt(getIntent().getSerializableExtra("conversationID").toString());
        c = DressCheckApplication.CMP.getConversationFromID(conversationID);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_conversation);

        ((TextView) (findViewById(R.id.nameHolderForConver))).setText(c.getChatterName());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        chatMessages = (ListView)findViewById(R.id.messageList);
        chatMessages.setAdapter(new ConversationAdapter(this, c.getMessages(), c.getChatterName()));
        chatMessages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardInListener();
                return false;
            }
        });

        messageText = (EditText)findViewById(R.id.messageText);
        messageSend = (Button) findViewById(R.id.messageSend);
        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageText.getText().length() != 0) {
                    String text = messageText.getText().toString();
                    String user = DressCheckApplication.CMP.getClient().getUsername();

                    c.addMessage(user, text);
                    c.sendToParse();

                    messageText.setText("");

                    hideKeyboardInListener();

                }
            }
        });


        updateManager = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        c = DressCheckApplication.CMP.updateConversation(c.getConversationID(), c);
                        c.updateFromParse();

                        if(c.getMessages().size() != chatMessages.getAdapter().getCount()) {
                            MyLog.print("ENtered Size Check");
                            Runnable updateUI = new Runnable() {
                                public void run() {
                                    MyLog.print("Running adapter from message change");
                                    resetAdapter();
                                }
                            };
                            runOnUiThread(updateUI);
                        }

                        Thread.sleep(2000);

                    }catch (Exception e){e.printStackTrace(); return;}

                }
            }
        };

        new Thread(updateManager).start();

        if(DressCheckApplication.tutorialCount_chat > 0){
            createDialogAlert();
            DressCheckApplication.tutorialCount_chat--;
        }

    }

    private void resetAdapter(){
        MyLog.print("Messages Updates:\t"+ c.getMessages());
        chatMessages.setAdapter(new ConversationAdapter(this, c.getMessages(), c.getChatterName()));

    }

    public void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(ConversationSubactivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_conversation, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConversationSubactivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void hideKeyboardInListener(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
