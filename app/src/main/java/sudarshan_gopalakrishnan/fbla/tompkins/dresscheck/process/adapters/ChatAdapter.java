package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.method.KeyListener;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.ChatActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.ConversationSubactivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.TutorialActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Conversation;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;

public class ChatAdapter extends BaseAdapter {

    private ChatActivity activity;
    private ChatAdapter chatAdapter = this;
    private LayoutInflater inflater = null;
    private ArrayList<String> listOfPerson = DressCheckApplication.CMP.getUsers();

    public ChatAdapter(ChatActivity a) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return 1 + listOfPerson.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(position == 0) {
            v = inflater.inflate(R.layout.layout_editfield, null);
            final EditText searchBox = (EditText)v.findViewById(R.id.editText2);
            searchBox.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN){
                        if(keyCode == KeyEvent.KEYCODE_ENTER){
                            String filter = searchBox.getText().toString();
                            if(filter.trim().length() == 0 || filter.equals("")){
                                listOfPerson = DressCheckApplication.CMP.getUsers();
                            }

                            ArrayList<String> newList = new ArrayList<String>();
                            for(String name: DressCheckApplication.CMP.getUsers()) {
                                if (name.contains(filter))
                                    newList.add(name);
                            }
                            listOfPerson = newList;
                            activity.setAdapter(chatAdapter);

                            return true;
                        }
                    }
                    return false;
                }
            });
            ImageButton searchButton = (ImageButton)v.findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filter = searchBox.getText().toString();
                    if(filter.trim().length() == 0 || filter.equals("")){
                        listOfPerson = DressCheckApplication.CMP.getUsers();
                    }

                    ArrayList<String> newList = new ArrayList<String>();
                    for(String name: DressCheckApplication.CMP.getUsers()) {
                        if (name.contains(filter))
                            newList.add(name);
                    }
                    listOfPerson = newList;
                    activity.setAdapter(chatAdapter);
                }
            });


        }else{
            v = inflater.inflate(R.layout.layout_textfield_large, null);
            final TextView textView = (TextView)(v.findViewById(R.id.textView3));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Conversation chosenConversation = DressCheckApplication.CMP.findConversation(textView.getText().toString());
                    chatPanelProcess(chosenConversation, textView.getText().toString());


                }
            });


            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, activity.getResources().getDisplayMetrics()));

            if((position - 1)%2 == 0){
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setText(listOfPerson.get(position - 1));
                textView.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                textView.setTextColor(activity.getResources().getColor(R.color.white));

            }
            if ((position - 1)%2 == 1){
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setText(listOfPerson.get(position - 1));
                textView.setBackgroundColor(activity.getResources().getColor(R.color.magenta_3));
                textView.setTextColor(activity.getResources().getColor(R.color.white));
            }
        }


        return v;
    }

    private void chatPanelProcess(Conversation c, String otherUser){
        if(c == null){
            Conversation newConversation = new Conversation();
            newConversation.setUserOne(DressCheckApplication.CMP.getClient().getUsername());
            newConversation.setUserTwo(otherUser);
            newConversation.setConversationID(DressCheckApplication.CMP.getClient().nextAvailConversationNumber());
            newConversation.sendToParse();
            c = newConversation;
            DressCheckApplication.CMP.addConversation(c);
        }


        MyLog.print("Conversation Details:\t"+c.getConversationID());
        try {
            c.updateFromParse();
        }catch (Exception e){e.printStackTrace();}

        Intent i = new Intent(activity.getApplicationContext(), ConversationSubactivity.class);
        i.putExtra("conversationID", c.getConversationID());
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activity.startActivity(i);
    }
}