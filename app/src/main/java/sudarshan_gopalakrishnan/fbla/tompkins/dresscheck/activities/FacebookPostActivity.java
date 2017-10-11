package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.BitmapHelperTools;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.splash.SendingPictureActivitySplash;

/**
 * Created by Sudarshan on 2/3/2016.
 */
public class FacebookPostActivity extends Activity{

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blank);

        FacebookSdk.sdkInitialize(getApplicationContext());

        final String messageToPost = getIntent().getSerializableExtra("messageToPost").toString();
        final String isAttire = getIntent().getSerializableExtra("isAttire").toString();
        final String peopleToSend = getIntent().getSerializableExtra("peopleToSend").toString();


        callbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions");
        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, permissionNeeds);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sharePhotoToFacebook(messageToPost);

                Intent i = new Intent(getApplicationContext(), SendingPictureActivitySplash.class);
                i.putExtra("StringMessage", "Uploading Your Picture");
                i.putExtra("messageToPost", messageToPost);
                i.putExtra("isAttire", isAttire);
                i.putExtra("peopleToSend", peopleToSend.toString());
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                Intent i = new Intent(getApplicationContext(), SendingPictureActivitySplash.class);
                i.putExtra("StringMessage", "Uploading Your Picture");
                i.putExtra("messageToPost", messageToPost);
                i.putExtra("isAttire", isAttire);
                i.putExtra("peopleToSend", peopleToSend.toString());
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Intent i = new Intent(getApplicationContext(), SendingPictureActivitySplash.class);
                i.putExtra("StringMessage", "Uploading Your Picture");
                i.putExtra("messageToPost", messageToPost);
                i.putExtra("isAttire", isAttire);
                i.putExtra("peopleToSend", peopleToSend.toString());
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    private void sharePhotoToFacebook(String messageToPost){
        Bitmap image = BitmapHelperTools.rotateBitmap(DressCheckApplication.CMP.getRecentImage(), 270);
        image = BitmapHelperTools.flipBitmap(image, true);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(messageToPost)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}
