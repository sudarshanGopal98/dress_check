package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process;

import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;

/**
 * Created by Sudarshan on 2/1/2016.
 */
public class NotificationSender{
    public NotificationSender(String usersData, final int postID){
        final ArrayList<String> users = decodeArrayList(usersData);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String user: users){
                    addUserNotification(user, postID);
                }
            }
        }).start();
    }

    private void addUserNotification(String user, int postID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
        query.whereEqualTo("username", user);
        try{
            Client client = (Client)query.getFirst();
            client.updateFromParse();
            client.addPostNotification(postID);
            client.sendToParse();

            MyLog.print("Completed Sending Notifications");

        }catch(com.parse.ParseException e){
            e.printStackTrace();
            return;
        }
    }

    public ArrayList<String> decodeArrayList(String data){
        ArrayList<String> toReturn = new ArrayList<>();
        data = data.replaceAll("\\[", "").replaceAll("\\]","");
        String[] data1 = data.split(",");
        for(String s: data1){
            try {
                toReturn.add(s);
            }catch(Exception e){}
        }
        return toReturn;
    }

}
