package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.ConversationSubactivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.KeyValueStorage;

public class ConversationAdapter extends BaseAdapter {
    private ConversationSubactivity activity;
    private LayoutInflater inflater = null;
    private KeyValueStorage<String, String> messages;
    private String chatterName;
    private String myName;

    public ConversationAdapter(ConversationSubactivity a, KeyValueStorage<String, String> messages, String chatterName) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messages = messages;
        this.chatterName = chatterName;
        myName = DressCheckApplication.CMP.getClient().getUsername();
    }


    public int getCount() {
        return messages.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        String user = messages.getKeyAt(position);
        String message = messages.getValueAt(position);

        if(user.equals(chatterName)){
            view = inflater.inflate(R.layout.layout_textfield_conversation_left_align, null);
            TextView textView = (TextView)view.findViewById(R.id.left_aligned_tv);
            textView.setText(message);

            textView.measure(0, 0);       //must call measure!
            int currentWidth = textView.getMeasuredWidth();
            textView.setWidth((int)(Math.round(1.5*currentWidth)));
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

        }else{
            view = inflater.inflate(R.layout.layout_textfield_conversation_right_align, null);
            TextView textView = (TextView)view.findViewById(R.id.right_aligned_tv);
            textView.setText(message);

            textView.measure(0, 0);       //must call measure!
            int currentWidth = textView.getMeasuredWidth();
            textView.setWidth((int) (Math.round(2 * currentWidth)));
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

        }


        return view;
    }

}