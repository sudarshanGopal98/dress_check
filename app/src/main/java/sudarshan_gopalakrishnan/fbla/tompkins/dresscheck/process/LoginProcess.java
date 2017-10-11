package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.util.Scanner;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.LoginActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.TutorialActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.LoginSplash;

/**
 * Created by Sudarshan on 12/12/2015.
 */
public class LoginProcess extends AsyncTask<Void, Void, String> {

    private LoginSplash activty;

    public LoginProcess(LoginSplash activty){
        this.activty = activty;
    }

    @Override
    protected String doInBackground(Void... params) {
        if(!deviceIsOnline()){
            return "NoInternet";
        }

        checkForMemoryLogin();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null && result.equals("NoInternet")){
            new CountDownTimer(10000, 2000) {
                public void onTick(long millisUntilFinished) {
                    Toast.makeText(activty, "No Internet Connection Found!\nPlease Connect To The Internet And Try Again!\n(The App Will Close In "+millisUntilFinished/2000+" seconds)", Toast.LENGTH_SHORT).show();

                }

                public void onFinish() {
                    System.exit(0);
                }
            }.start();
        }
    }

    private boolean deviceIsOnline(){
        try {
            InetAddress ipAddress = InetAddress.getByName("www.google.com");

            if (ipAddress.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    private void checkForMemoryLogin(){
        MyLog.print("Entered Check");
        try {
            if(tutorialCheck()){
                MyLog.print("TEST");
                return;
            }

            MyLog.print("Did not do tutorial check");
            File file = new File(activty.getFilesDir(), "DressCheckLogin");
            MyLog.print("MAdeFIle");

            Scanner scanner = new Scanner(file);
            MyLog.print("madescanner");

            if (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(";");
                if(checkLogin(data[0], data[1])){
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
                    query.whereEqualTo("username", data[0]);
                    try{
                        Client client = (Client) query.getFirst();

                        if(client.get("password").equals(data[1])){
                            client.updateFromParse();
                            DressCheckApplication.CMP.setClient(client);

                            Intent i = new Intent(activty.getApplicationContext(), WelcomeActivity.class);
                            activty.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            activty.startActivity(i);
                            scanner.close();
                            activty.finish();



                        } else {
                            scanner.close();
                            openLoginActivity();

                        }

                    }catch(com.parse.ParseException e){
                        scanner.close();
                        openLoginActivity();

                        return;
                    }
                }else{
                    scanner.close();
                    openLoginActivity();
                }
            }
        }catch(Exception e){
            openLoginActivity();
        }
    }

    public boolean checkLogin(String usernameText, String passwordText){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
        query.whereEqualTo("username", usernameText);
        try{
            ParseObject client = query.getFirst();

            if(client.get("password").equals(passwordText)){
                return true;

            }else{
                return false;
            }

        }catch(com.parse.ParseException e){
            return false;
        }
    }

    private boolean tutorialCheck(){
        try {
            File tutorialCheck = new File(activty.getFilesDir(), "TutorialCheck");
            if (tutorialCheck.exists() == false) {
                MyLog.print("TEST1");
                tutorialCheck.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(tutorialCheck));
                bw.write("check");
                bw.flush();
                bw.close();

                Intent i = new Intent(activty.getApplicationContext(), TutorialActivity.class);
                activty.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                activty.startActivity(i);
                activty.finish();
                return true;

            }else{
                MyLog.print("TEST2");
                Scanner fromFile = new Scanner(tutorialCheck);
                String checkString = fromFile.nextLine();
                fromFile.close();
                MyLog.print(checkString);

                if(checkString.equals("check")){
                    Intent i = new Intent(activty.getApplicationContext(), TutorialActivity.class);
                    activty.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    activty.startActivity(i);
                    activty.finish();
                    return true;
                }
                return false;
            }


        }catch(Exception e){
            return false;
        }
    }

    private void openLoginActivity(){
        Intent i = new Intent(activty.getApplicationContext(), LoginActivity.class);
        activty.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activty.startActivity(i);
        activty.finish();
    }
}
