package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.SinglePostDisplayActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.BitmapHelperTools;

public class PostFeedAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Post> postArrayList;
    private static LayoutInflater inflater=null;

    public PostFeedAdapter(Activity a, ArrayList<Post> postArrayList) {
        activity = a;
        this.postArrayList = postArrayList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        if(postArrayList != null)
            return postArrayList.size();
        else return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_post_horizontal, null);

        final TextView question = (TextView)vi.findViewById(R.id.postQuestionHolder);
        final EditText comments = (EditText) vi.findViewById(R.id.newCommentText);
        final Button post = (Button) vi.findViewById(R.id.postButton2);
        final Post currentPost = postArrayList.get(position);
        final ToggleButton likeButton = (ToggleButton) vi.findViewById(R.id.postButton);

        ImageView pictureDisplay =(ImageView)vi.findViewById(R.id.postImageDisplay);

        question.setText(postArrayList.get(position).toString());
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    currentPost.addComment(comments.getText().toString(), DressCheckApplication.CMP.getClient().getUsername());
                    currentPost.sendToParse();
                    currentPost.updateFromParse();
                    comments.setText("");
                    createSinglePostDisplay(currentPost);

                } catch (Exception e) {e.printStackTrace();}
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  try {
                      if(likeButton.isChecked()){
                          likeButton.setBackgroundColor(activity.getResources().getColor(R.color.purple_5));
                          currentPost.addLike();
                          currentPost.sendToParse();
                          currentPost.updateFromParse();
                      } else {
                          likeButton.setBackgroundColor(activity.getResources().getColor(R.color.purple_3));
                          currentPost.removeLike();
                          currentPost.sendToParse();
                          currentPost.updateFromParse();
                      }
                  }catch(Exception e){e.printStackTrace();}
              }
          });


        Bitmap imageToUse = postArrayList.get(position).getImage();
        imageToUse = BitmapHelperTools.flipBitmap(imageToUse, true);
        imageToUse = BitmapHelperTools.rotateBitmap(imageToUse, 270);
        imageToUse = BitmapHelperTools.getResizedBitmap(imageToUse, activity.getWindowManager().getDefaultDisplay().getWidth());
        pictureDisplay.setImageBitmap(imageToUse);

        pictureDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSinglePostDisplay(currentPost);
            }
        });

        switch (position%5){
            case 0: vi.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                pictureDisplay.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                comments.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                question.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                break;
            case 1: vi.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                pictureDisplay.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                comments.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                question.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                break;
            case 2: vi.setBackgroundColor(activity.getResources().getColor(R.color.magenta_3));
                pictureDisplay.setBackgroundColor(activity.getResources().getColor(R.color.magenta_3));
                comments.setBackgroundColor(activity.getResources().getColor(R.color.magenta_3));
                question.setBackgroundColor(activity.getResources().getColor(R.color.magenta_3));
                break;
            case 3: vi.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                pictureDisplay.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                comments.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                question.setBackgroundColor(activity.getResources().getColor(R.color.magenta_4));
                break;
            case 4: vi.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                pictureDisplay.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                comments.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                question.setBackgroundColor(activity.getResources().getColor(R.color.magenta_5));
                break;

        }

        return vi;
    }
    private void createSinglePostDisplay(Post currentPost){
        Intent i = new Intent(activity.getApplicationContext(), SinglePostDisplayActivity.class);
        i.putExtra("postID",currentPost.getPostID());
        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}