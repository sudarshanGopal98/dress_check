package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.LargePostDisplayActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.BitmapHelperTools;

public class PastPostsAdapter extends BaseAdapter {

    private LargePostDisplayActivity largePostDisplayActivity;
    private Activity activity;
    private Post p;
    private static LayoutInflater inflater=null;

    public PastPostsAdapter(LargePostDisplayActivity a, Post p) {
        largePostDisplayActivity = a;
        this.p = p;
        activity = (Activity) largePostDisplayActivity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public int getCount() {
        return 4 + p.getComments().size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        MyLog.print("pos: " + position);

        if(position == 0) {
            v = inflater.inflate(R.layout.layout_plainimageview, null);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageView2);
            Bitmap imageToUse = p.getImage();
            imageToUse = BitmapHelperTools.flipBitmap(imageToUse, true);
            imageToUse = BitmapHelperTools.rotateBitmap(imageToUse, 270);
            imageToUse = BitmapHelperTools.getResizedBitmap(imageToUse, activity.getWindowManager().getDefaultDisplay().getWidth());
            imageView.setImageBitmap(imageToUse);

        }else if(position == 1){
            v = inflater.inflate(R.layout.layout_post, null);
            final TextView commentText = (EditText)v.findViewById(R.id.editText);
            Button postReply = (Button)v.findViewById(R.id.postReply);
            postReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        p.addComment(commentText.getText().toString(), DressCheckApplication.CMP.getClient().getUsername());
                        p.sendToParse();
                        p.updateFromParse();
                        commentText.setText("");
                        largePostDisplayActivity.updateAdapter();
                    }catch (Exception e){e.printStackTrace();}
                }
            });
        }else if(position == 2) {
            v = inflater.inflate(R.layout.layout_textfield_medium, null);
            TextView textView = (TextView)v.findViewById(R.id.textView3);

            int likes = p.getLike();
            if(likes != 1)
                textView.setText(likes + " Likes!");
            else textView.setText(likes + " Like!");
            textView.setBackgroundColor(activity.getResources().getColor(R.color.magenta_2));
            textView.setTextSize(22);
            textView.setTextColor(Color.WHITE);


        }else if(position == 3) {
            v = inflater.inflate(R.layout.layout_textfield_medium, null);
            TextView textView = (TextView)v.findViewById(R.id.textView3);

            int commentsSize = p.getComments().size();
            if(commentsSize != 1)
                textView.setText(commentsSize + " comments were posted!");
            else textView.setText(commentsSize + " comment was posted!");

            textView.setBackgroundColor(activity.getResources().getColor(R.color.purple_3));
            textView.setTextSize(22);
            textView.setTextColor(Color.WHITE);


        }else{
            v = inflater.inflate(R.layout.layout_textfield_medium, null);
            TextView postComment = (TextView)v.findViewById(R.id.textView3);

            String comment = postCommentProcess(p.getComments().get(position - 4));
            postComment.setText("     "+comment+"     ");

            if ((position - 1) % 2 == 1) {
                postComment.setBackgroundColor(activity.getResources().getColor(R.color.purple_6));
                postComment.setAlpha((float)0.65);
            } else {
                postComment.setBackgroundColor(activity.getResources().getColor(R.color.purple_5));
                postComment.setAlpha((float)0.65);
            }
            postComment.setTextColor(Color.WHITE);


            if(comment.equals(p.getComments().get(position - 4))){
                postComment.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                postComment.setAlpha((float)0.5);
            }else postComment.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);



        }


        return v;
    }
    private String postCommentProcess(String s){
        String[] dat1 = s.split(":");
        if(dat1.length == 2) {
            if (dat1[0].contains(DressCheckApplication.CMP.getClient().getUsername())) {
                return "You Said: "+dat1[1];
            }
        }

        return s;
    }



}