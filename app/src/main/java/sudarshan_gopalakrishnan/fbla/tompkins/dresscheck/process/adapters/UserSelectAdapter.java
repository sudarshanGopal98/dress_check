package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.ConversationSubactivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.PeopleToSendSubactivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Conversation;

public class UserSelectAdapter extends BaseAdapter {
    private PeopleToSendSubactivity activity;
    private UserSelectAdapter userSelectAdapter = this;
    private LayoutInflater inflater = null;
    private ArrayList<String> listOfPerson = DressCheckApplication.CMP.getUsers();
    private ArrayList<Boolean> checkManager = new ArrayList<>();

    public UserSelectAdapter(PeopleToSendSubactivity a) {
        this.activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0; i<getCount(); i++){
            checkManager.add(false);
        }

    }


    public int getCount() {
        return listOfPerson.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=convertView;
        v = inflater.inflate(R.layout.layout_checkbox, null);

        final CheckBox checkBox = (CheckBox)v.findViewById(R.id.checkBox);
        checkBox.setChecked(checkManager.get(position));
        checkBox.setText(listOfPerson.get(position));
        checkBox.setTextColor(activity.getResources().getColor(R.color.white));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    activity.addPerson(checkBox.getText().toString());
                    checkManager.set(position, true);

                }else {
                    activity.removePerson(checkBox.getText().toString());
                    checkManager.set(position, false);
                }
            }
        });


        return v;
    }

}