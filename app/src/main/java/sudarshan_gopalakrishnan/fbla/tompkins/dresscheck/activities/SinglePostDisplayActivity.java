package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.LargePostDisplayActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.adapters.PastPostsAdapter;


public class SinglePostDisplayActivity extends Activity implements LargePostDisplayActivity {

    private ListView listView;
    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_single_post);

        postID = getIntent().getSerializableExtra("postID").toString();

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new PastPostsAdapter(this, DressCheckApplication.CMP.getPost(postID)));

        TextView postHolder = (TextView) findViewById(R.id.postHeader);
        postHolder.setText(DressCheckApplication.CMP.getPost(postID).getMessage());

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.gc();
    }

    @Override
    public void updateAdapter() {
        listView.setAdapter(new PastPostsAdapter(this, DressCheckApplication.CMP.getPost(postID)));
    }
}
