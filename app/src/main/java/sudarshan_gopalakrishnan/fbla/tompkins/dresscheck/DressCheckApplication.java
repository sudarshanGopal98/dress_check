package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Conversation;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.ClientMainProcess;

/**
 * Created by othscs001 on 12/4/2015.
 */
public class DressCheckApplication extends Application {

    public static ClientMainProcess CMP = new ClientMainProcess();
    public static int tutorialCount_postfeed = 1;
    public static int tutorialCount_myposts = 1;
    public static int tutorialCount_camera = 1;
    public static int tutorialCount_picturedisplay = 1;
    public static int tutorialCount_selectfriends = 1;
    public static int tutorialCount_chat = 1;
    public static int tutorialCount_conversation = 1;



    public void onCreate(){
        super.onCreate();
        CMP.setApplicationContext(this);


        ParseObject.registerSubclass(Client.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Conversation.class);
        Parse.initialize(this, "V239w6T2KYLXeCScPHgCLMsGWNNvig75dhTNqhyS", "SoWB9CBvHAxGrmo7tp2rFtZJouGzok61kCSddkr0");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        FacebookSdk.sdkInitialize(getApplicationContext());



        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "sudarshan_gopalakrishnan.fbla.tompkins.dresscheck",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        Thread shutdownProcess = new Thread(new Runnable() {
            @Override
            public void run() {
                if(CMP != null  && CMP.getClient() != null)
                    CMP.getClient().sendToParse();
            }
        });


        Runtime.getRuntime().addShutdownHook(shutdownProcess);
    }


    public static void resetTutorialCount(){
        tutorialCount_postfeed = 1;
        tutorialCount_myposts = 1;
        tutorialCount_camera = 1;
        tutorialCount_picturedisplay = 1;
        tutorialCount_selectfriends = 1;
    }

}
