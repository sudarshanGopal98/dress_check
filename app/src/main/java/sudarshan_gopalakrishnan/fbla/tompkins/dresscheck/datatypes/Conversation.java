package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;

/**
 * Created by othscs001 on 1/25/2016.
 */
@ParseClassName("Conversation")
public class Conversation extends ParseObject implements Parsable, Serializable{
    int conversationID = 0;
    String userOne, userTwo;
    KeyValueStorage<String, String> messages = new KeyValueStorage<>();

    public Conversation(){
        messages = new KeyValueStorage<>();
        userOne = "";
        userTwo = "";
    }


    @Override
    public void updateFromParse() throws com.parse.ParseException{
        userOne = get("userOne").toString();
        userTwo = get("userTwo").toString();
        conversationID = Integer.parseInt(get("conversationID").toString());
        String KeyValueStorageDetails = get("messages").toString();
        messages = createKeyValueStorage(KeyValueStorageDetails);

        MyLog.print("Messages after update:"+messages);

    }

    @Override
    public void sendToParse(){
        try {
            this.put("conversationID", conversationID);
            this.put("userOne", userOne);
            this.put("userTwo", userTwo);
            this.put("messages", messages.createStringStorageForParse());
            this.save();

        }catch (Exception e){e.printStackTrace();}

    }

    private KeyValueStorage<String, String> createKeyValueStorage(String data){
        KeyValueStorage<String, String> toReturn = new KeyValueStorage<>();

        data = data.replaceAll("\\[", "").replaceAll("\\]","");
        String[] data1 = data.split(";");
        for(String data2: data1){
            try {
                String[] data3 = data2.split(",");
                toReturn.add(data3[0], data3[1]);
            }catch(Exception e){}
        }

        return toReturn;
    }

    public String getChatterName(){
        String myName = DressCheckApplication.CMP.getClient().getUsername();

        if(myName.equals(userOne))
            return userTwo;
        else if(myName.equals(userTwo))
            return userOne;
        else
            return null;

    }

    public void addMessage(String user, String message){
        messages.add(user, message);
    }

    public int size(){
        return messages.size();
    }

    public String getUserOne() {
        return userOne;
    }

    public void setUserOne(String userOne) {
        this.userOne = userOne;
    }

    public String getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(String userTwo) {
        this.userTwo = userTwo;
    }

    public int getConversationID() {
        return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public KeyValueStorage<String, String> getMessages() {
        return messages;
    }

    public void setMessages(KeyValueStorage messages){
        this.messages = messages;
    }

    public void test() {
        addMessage(userOne, "hi");
        addMessage(userOne, "hi");
        addMessage(userTwo, "how are you");
        addMessage(userOne, "im fine");
        addMessage(userOne, "nice");
        sendToParse();

    }
}
