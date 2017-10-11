package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.LoginActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.WelcomeActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Client;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Conversation;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;

/**
 * Created by Sudarshan on 12/5/2015.
 */
public class ClientMainProcess {
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Post> myPosts;
    private ArrayList<String> users;
    private ArrayList<Conversation> conversations;
    private ArrayList<Post> postNotifications;

    private Client client;
    private byte[] recentImage;
    private boolean isAttireFilter = true;

    private DressCheckApplication applicationContext;


    public Client getClient(){
        return client;
    }

    public void setClient(Client c){
        client = c;
        updateNotification();
    }

    public Post getPost(String id){
        for(Post p: getAllPosts()){
            if(p.getPostID() == Integer.parseInt(id))
                return p;
        }
        return null;
    }

    public ArrayList<String> getUsers(){
        return users;
    }

    public void setApplicationContext(DressCheckApplication applicationContext) {
        this.applicationContext = applicationContext;
    }

    public DressCheckApplication getApplicationContext(){
        return applicationContext;
    }

    public ArrayList<Post> getAllPosts() {
        return posts;
    }

    public boolean updatePosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.setLimit(50);
        query.orderByDescending("postID");
        query.whereEqualTo("isAttire", isAttireFilter);
        query.findInBackground(new FindCallback() {

            @Override
            public void done(List objects, com.parse.ParseException e) {
                this.done(objects, e);
            }

            @Override
            public void done(Object o, Throwable throwable) {
                try {
                    if (throwable != null) {
                        throw new com.parse.ParseException(throwable);
                    }

                    for (Post p : (ArrayList<Post>) (o)) {
                        p.updateFromParse();
                    }
                    posts = (ArrayList<Post>) o;

                } catch (com.parse.ParseException e) {
//                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public void setRecentImage(Bitmap bmp){
        recentImage = BitmapHelperTools.convertToByteArray(bmp);
    }

    public Bitmap getRecentImage(){
        return BitmapHelperTools.convertToBitmap(recentImage);
    }

    public Conversation findConversation(String otherUser){
        String me = client.getUsername();

        for(Conversation c: conversations){
            if(c.getUserOne().equals(me)    ||  c.getUserTwo().equals(me)){
                if(c.getUserOne().equals(otherUser) || c.getUserTwo().equals(otherUser)){
                    return c;
                }
            }
        }
        return null;
    }

    public Conversation getConversationFromID(int conversationID){
        for(Conversation c: conversations){
            if(c.getConversationID() == conversationID){
                return c;
            }
        }
        return null;
    }

    public ArrayList<Conversation> getAllConversations(){
        return conversations;
    }

    public ArrayList<Post> requestMyPosts(){

        if(myPosts == null  || myPosts.isEmpty()){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            myPosts = new ArrayList<Post>();

            query.setLimit(20);
            String username = DressCheckApplication.CMP.getClient().getUsername();
            query.whereEqualTo("userWhoPosted", username.toString());
            query.orderByDescending("postID");
            try {
                for (ParseObject parseObject : query.find()) {
                    Post p = (Post) parseObject;
                    p.updateFromParse();
                    myPosts.add(p);
                }
            } catch (Exception e) {}
        }




        return myPosts;
    }

    public void clearMyPosts(){
        if(myPosts != null){
            myPosts = null;
            System.gc();
        }
    }

    public void clearAllPosts(){
        if(posts != null){
            posts = null;
            System.gc();
        }
    }

    public boolean logoutProcess(Activity refActivity){

        File file = new File(refActivity.getFilesDir(), "DressCheckLogin");
        if(file.delete()){
            client = null;
            file = new File(refActivity.getFilesDir(), "TutorialCheck");
            file.delete();

            return true;
        }else return false;
    }

    public ArrayList<String> updateUsers(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Client");
        users =  new ArrayList<>();

        try {
            query.findInBackground(new FindCallback <ParseObject> () {
                public void done (List <ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for(ParseObject po: objects){
                            if(!users.contains(po.toString())) {
                                String username = po.get("username").toString();
                                if(!username.equals(client.getUsername()))
                                    users.add(username);
                            }
                        }


                    } else {
                        e.printStackTrace();
                    }
                    MyLog.print("Users:"+users.toString());
                }
            });

        }catch (Exception ex){ex.printStackTrace();}
        Collections.sort(users);
        return users;
    }

    public ArrayList<String> updateConversations(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Conversation");
        conversations =  new ArrayList<>();

        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        MyLog.print("Objects:" + objects);
                        for (ParseObject po : objects) {
                            try {
                                Conversation received = (Conversation) po;
                                received.updateFromParse();
                                conversations.add(received);

                            } catch (Exception ex) {
                                MyLog.print("QUERY:" + query);

                                ex.printStackTrace();
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception ex){ex.printStackTrace();}

        return users;
    }

    public boolean addConversation(Conversation conversation){
        for(Conversation c: conversations){
            if(c.getConversationID() == conversation.getConversationID()){
                return false;
            }
        }
        return conversations.add(conversation);
    }

    public Conversation updateConversation(int conversationID, Conversation currentConversation){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Conversation");
        query.whereEqualTo("conversationID", conversationID);
        query.addDescendingOrder("updatedAt");
        try {
            return (Conversation)(query.getFirst());
        }catch (Exception ex){}

        return null;
    }

    public void updateNotification(){
        postNotifications = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.setLimit(25);
        query.orderByDescending("postID");
        query.whereEqualTo("isAttire", isAttireFilter);
        query.findInBackground(new FindCallback() {

            @Override
            public void done(List objects, com.parse.ParseException e) {
                if (e == null)
                    this.done(objects, (Throwable) e);
            }

            @Override
            public void done(Object o, Throwable throwable) {
                try {
                    if (throwable != null) {
                        throw new com.parse.ParseException(throwable);
                    }

                    for (Post p : (ArrayList<Post>) (o)) {
                        int currentPostID = Integer.parseInt(p.get("postID").toString());
                        if (client.hasPostNotification(currentPostID)) {
                            client.removePostNotification(currentPostID);
                            p.updateFromParse();
                            postNotifications.add(p);
                            MyLog.print("TO RETURN:" + postNotifications);
                        }
                    }
                } catch (com.parse.ParseException e) {
//                    e.printStackTrace();
                }
            }
        });

    }

    public ArrayList<Post> getPostNotifications(){
        return postNotifications;
    }

    public void clearNotifications(){
        postNotifications.clear();
        client.clearNotifications();

        client.sendToParse();
    }


    public boolean isAttireFilter() {
        return isAttireFilter;
    }

    public void setIsAttireFilter(boolean isAttireFilter){
        this.isAttireFilter = isAttireFilter;
    }
}


