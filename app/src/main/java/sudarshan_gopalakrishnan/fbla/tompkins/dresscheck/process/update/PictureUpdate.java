package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.update;

import android.os.AsyncTask;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Post;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SplashActivity;

/**
 * Created by Sudarshan on 12/12/2015.
 */
public class PictureUpdate extends AsyncTask<Post, Void, Void> {

    private SplashActivity activityToUpdate;

    public PictureUpdate(SplashActivity activityToUpdate){
        this.activityToUpdate = activityToUpdate;
    }

    @Override
    protected Void doInBackground(Post... params) {
        params[0].sendToParse();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        activityToUpdate.updateCompleted();
    }

}
