package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.BitmapHelperTools;

/**
 *
 * The object Post is an object which stores all information related to a post such as the image, the message/question that the user wanted
 * to ask, the comments and the post id.
 *
 * @author Sudarshan
 */
@ParseClassName("Post")
public class Post extends ParseObject implements Parsable{

    private byte[] image;
    private String message;
    private ArrayList<String> comments;
    private int postID;
    private String userWhoPosted;
    private boolean isAttire;
    private int like;


    /**
     * The method sendToParse() sends all data stored locally to the web database.
     */
    public void sendToParse(){
        try {
            ParseFile imageFile = new ParseFile(image, "imageData");

            this.put("image", imageFile);
            this.put("message", message);
            this.put("comments", comments.toString());
            this.put("postID", postID);
            this.put("userWhoPosted",userWhoPosted);
            this.put("isAttire",isAttire);
            this.put("like", like);
            this.save();
        }catch(Exception e){
            e.printStackTrace();
            MyLog.print(e);
        }
    }

    /**
     * This method gets all the data from the database and stores it locally for domestic access.
     * @throws com.parse.ParseException The method throws this exception the program fails to create a connection with the online database.
     */
    @Override
    public void updateFromParse() throws com.parse.ParseException{
        setMessage(get("message").toString());
        setComments(get("comments").toString());
        setPostID(get("postID").toString());
        setUserWhoPosted(get("userWhoPosted").toString());
        setIsAttire(Boolean.parseBoolean(get("isAttire").toString()));
        ParseFile imageFile = (ParseFile) (get("image"));
        setImage(imageFile.getData());
        setLike(get("like").toString());
    }

    /**
     *
     * @return Returns the image associated with the Post.
     */
    public Bitmap getImage() {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * @return Returns the message associated with the post.
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return Returns all the comments made about the post in form of an ArrayList
     */
    public ArrayList<String> getComments() {
        return comments;
    }

    /**
     * @return Returns the post ID associated with the post of interest.
     */
    public int getPostID() {
        return postID;
    }

    /**
     * @return Returns true if the Post is regarding the user's attire. Returns false if the post is about general fashion.
     */
    public boolean isAttire() {
        return isAttire;
    }

    /**
     * @return Returns the number of likes the picture received
     */
    public int getLike(){
        return like;
    }

    /**
     * The method receives a new image, converts it into a byte array and stores it into the current instance of post.
     * @param image Bitmap image is the new image that is to be associated with the post.
     */
    public void setImage(Bitmap image) {
        this.image = BitmapHelperTools.convertToByteArray(image);
    }

    /**
     *
     * @param bitmapdata byte[] bitmapdata is the data that is used to create a Bitmap image; this data is stored directly into the current
     *                   instance of post
     */
    public void setImage(byte[] bitmapdata){
        this.image = bitmapdata;
    }

    /**
     * Sets the parameter as the message or question conveyed/asked by the post
     * @param message String message is the new message associated with the post.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Receives a String which was initially produced by the toString() method of the ArrayList<String> comments, and stores each element
     * originally present in the ArrayList into the current instance of Post.
     *
     * @param commentString String commentString is the toString of an ArrayList<String> that was initially contained comments.
     */
    public void setComments(String commentString) {
        comments = new ArrayList<String>();
        String temp = commentString.substring(1,commentString.length()-1);
        String[] temp2 = temp.split(",");
        for(String temp3: temp2){
            temp3 = temp3.trim();
            if(!temp3.equals(null)  &&  temp3.length() > 0) {
                comments.add(temp3);
            }
        }
    }

    /**
     * Sets the post ID of the current instance to the parameter
     * @param postID int postID is the new Post ID
     */
    public void setPostID(String postID) {
        this.postID = Integer.parseInt(postID);
    }

    /**
     * Sets the user who posted the post to the parameter.
     * @param userWhoPosted String userWhoPosted is the user who posted the post.
     */
    public void setUserWhoPosted(String userWhoPosted){
        this.userWhoPosted = userWhoPosted;
    }

    /**
     * Sets if the post is about the user's attire or fashion in general.
     * @param isAttire boolean isAttire depicts whether the post is about attire or general fashion
     */
    public void setIsAttire(boolean isAttire) {
        this.isAttire = isAttire;
    }

    /**
     * Sets the number of likes the photo has recieved
     * @param like int like is the number of likes the picture has
     */
    public void setLike(String like){
        this.like = Integer.parseInt(like);
    }

    /**
     * Adds the comment made by a user in the following format: "user" said: "comment"
     * @param comment String comment is the comment made about the post.
     * @param user String user is the name of the user who posted the post.
     */
    public void addComment(String comment, String user) {
        if(comment.length()== 0)
            return;
        comments.add(0, user + " said: " + comment);
    }

    /**
     * The method adds a like to the post
     */
    public void addLike(){
        like++;
    }

    /**
     * The method removes a like from the post
     */
    public void removeLike(){
        like--;
    }


    @Override
    public String toString(){
        if(userWhoPosted.equals(DressCheckApplication.CMP.getClient().getUsername())){
            return "You asked: "+message;
        }else{
            return userWhoPosted+" asked: "+message;
        }
    }
}
