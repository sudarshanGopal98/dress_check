package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;

public class LoginActivity extends Activity {

    private Button login, signup;
    private EditText username, password;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);

        handler = new Handler(Looper.myLooper());

        username = (EditText)findViewById(R.id.usernameText);
        username.requestFocus();
        password = (EditText)findViewById(R.id.passwordText);


        login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String usernameText = username.getText().toString();
                        String passwordText = password.getText().toString();

                        if(usernameText.length() == 0 || passwordText.length() == 0){
                            createNothingEnteredDialogAlert();
                            return;
                        }

                        loginProcess(usernameText, passwordText);
                    }


                };
                handler.post(runnable);
            }
        });

        signup = (Button)findViewById(R.id.createAccount);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateAccountSubactivity.class);
                startActivity(i);
            }
        });
    }


    public void loginProcess(String usernameText, String passwordText){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
        query.whereEqualTo("username", usernameText);
        try{
            Client client = (Client) query.getFirst();

            if(client.get("password").equals(passwordText)){
                try{
                    File file = new File(LoginActivity.this.getFilesDir(), "DressCheckLogin");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    bw.write(usernameText+";"+passwordText);
                    bw.flush();
                    bw.close();
                }catch(Exception e){
                    e.printStackTrace();
                }

                client.updateFromParse();
                DressCheckApplication.CMP.setClient(client);


                if(tutorialCheck()){
                    return;
                }


                Intent i = new Intent(getApplicationContext(),WelcomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                LoginActivity.this.finish();

            }else{
                createIncorrectPasswordDialogAlert();
            }

        }catch(com.parse.ParseException e){
            createNoAccountDialogAlert();
            return;
        }
    }

    private boolean tutorialCheck(){
        try {
            File tutorialCheck = new File(getFilesDir(), "TutorialCheck");
            if (tutorialCheck.exists() == false) {
                tutorialCheck.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(tutorialCheck));
                bw.write("check");
                bw.flush();
                bw.close();

                Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(i);
                finish();
                return true;

            }else{
                Scanner fromFile = new Scanner(tutorialCheck);
                String checkString = fromFile.nextLine();
                fromFile.close();
                MyLog.print(checkString);

                if(checkString.equals("check")){
                    Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(i);
                    finish();
                    return true;
                }
                return false;
            }


        }catch(Exception e){
            return false;
        }
    }

    private void createIncorrectPasswordDialogAlert(){
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_failed, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    private void createNoAccountDialogAlert(){
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_noexist, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }

    private void createNothingEnteredDialogAlert(){
        LayoutInflater li = LayoutInflater.from(LoginActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_login_nothingentered, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }
}
