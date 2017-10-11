package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;

/**
 * Created by Sudarshan on 2/4/2016.
 */
public class CreateAccountSubactivity extends Activity {
    private Button signup;
    private EditText username, password;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login_dialog);
        username = (EditText)findViewById(R.id.usernameText);
        username.requestFocus();
        password = (EditText)findViewById(R.id.passwordText);

        signup = (Button)findViewById(R.id.createAccount);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

                if (usernameText.length() == 0 || passwordText.length() == 0) {
                    createNothingEnteredDialogAlert();
                    return;
                }

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
                query.whereEqualTo("username", usernameText);
                try {
                    query.getFirst();
                    createExistsDialogAlert();
                    return;
                } catch (Exception e) {
                    MyLog.print("Error:" + e);
                    MyLog.print("Free to Continue!");
                }

                MyLog.print("Client Creation Begins");

                Client newClient = new Client();
                newClient.setUsername(usernameText);
                newClient.setPassword(passwordText);
                newClient.sendToParse();

                MyLog.print("Client Creation Completed");

                createLoginCompleteDialogAlert();


            }
        });
    }

    private void createLoginCompleteDialogAlert(){
        LayoutInflater li = LayoutInflater.from(CreateAccountSubactivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_complete, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccountSubactivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    CreateAccountSubactivity.this.finish();
                }
                return true;
            }
        });
    }

    private void createNothingEnteredDialogAlert(){
        LayoutInflater li = LayoutInflater.from(CreateAccountSubactivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_nothingentered, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccountSubactivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    private void createExistsDialogAlert(){
        LayoutInflater li = LayoutInflater.from(CreateAccountSubactivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_alreadyexists, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateAccountSubactivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
}
