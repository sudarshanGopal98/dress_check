package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

/**
 * The Java Class Client is a custom data object which is used to store all information about a particular client.
 * The extension of ParseObject enables this custom class to store its data into the Parse database that is used to store
 * all information related to the program.
 *
 *
 * @author Sudarshan Gopalakrishnan, Obra D. Tompkins High School, Katy (Area 5), Texas
 */
@ParseClassName("Client")
public class Client extends ParseObject implements Parsable{

    /**
     * Attributes:
     *  String username: Stores the username of the client
     *  String password: Stores the password of the client
     *  int pictureCount: Temporarily stores the number of pictures posted by the client.
     *                    The data is this variable is not sent back to the server.
     */
    private String username, password;
    private int pictureCount, conversationID;
    private ArrayList<Integer> postsFromOthers;

    /**
     * A constructor with no arguements is mandated for compatibility with Parse.
     */
    public Client(){
        postsFromOthers = new ArrayList<>();
    }

    /**
     *
     * @return Returns the username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Replaces the current username with the parameter.
     * @param username The intended username of the client
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return Returns the password of the client.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Replaces the current password of the client with the parameter.
     * @param password The intended password of the client
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The method requests a query from the server, which sends back the ID of the latest post. This enables the program to make assign a ID to the
     * upcoming post made by the user using the program.
     *
     * @return Returns the next available picture number
     */
    public int nextAvailPicNumber(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.orderByDescending("createdAt");
        try{
            ParseObject post = query.getFirst();
            int postID = Integer.parseInt(post.get("postID").toString());
            MyLog.print("Post ID:"+postID);
            pictureCount = postID;

        }catch(com.parse.ParseException e){
            e.printStackTrace();
        }

        pictureCount++;
        return pictureCount;
    }

    /**
     * The method requests a query from the server, which sends back the ID of the latest conversation. This enables the program to make assign a ID to the
     * upcoming conversation made by the user using the program.
     *
     * @return Returns the next available conversation ID
     */
    public int nextAvailConversationNumber(){
        int tempID = 0;

        MyLog.print("TEST01");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Conversation");
        query.orderByDescending("createdAt");
        try{
            MyLog.print("TEST02");
            ParseObject conversation = query.getFirst();
            MyLog.print("TEST03");
            tempID = Integer.parseInt(conversation.get("conversationID").toString());

        }catch(com.parse.ParseException e){
            e.printStackTrace();
        }

        tempID++;
        return tempID;
    }


    /**
     * sendToParse() sends the data stored locally in the program to the server.
     */
    public void sendToParse(){
        try {
            this.put("username", username);
            this.put("password", password);
            this.put("pictureCount", pictureCount);
            this.put("postNotification", postsFromOthers.toString());

            MyLog.print("postNotificaiton before submit:" + postsFromOthers.toString());

            this.save();
        }catch (com.parse.ParseException e){
            e.printStackTrace();
        }
    }

    public void updateFromParse() {
        setUsername(get("username").toString());
        setPassword(get("username").toString());
        pictureCount = Integer.parseInt(get("pictureCount").toString());
        postsFromOthers = decodeArrayList(get("postNotification").toString());

        MyLog.print("Completed Client Update... PosrFromOthers=" + postsFromOthers);
    }

    public ArrayList<Integer> decodeArrayList(String data){
        ArrayList<Integer> toReturn = new ArrayList<>();
        data = data.replaceAll("\\[", "").replaceAll("\\]", "");
        String[] data1 = data.split(",");
        for(String s: data1){
            try {
                toReturn.add(Integer.parseInt(s));
            }catch(Exception e){}
        }
        return toReturn;
    }

    public void addPostNotification(int postID){
        postsFromOthers.add(postID);
    }

    public boolean hasPostNotification(int postID){
        return postsFromOthers.contains(postID);
    }

    public void removePostNotification(int postID){
        for(int i = 0; i < postsFromOthers.size(); i++){
            if(postsFromOthers.get(i) == postID){
                postsFromOthers.remove(i);
                return;
            }
        }
    }

    public boolean hasPostNotifications(){
        MyLog.print("Posts from others:"+postsFromOthers);
        return !postsFromOthers.isEmpty();
    }

    public void clearNotifications(){
        postsFromOthers.clear();
    }

    /**
     *
     * @return Returns a String with the username and password of the client.
     */
    public String toString(){
        return username+" :: "+password;
    }

}