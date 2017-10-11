package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.update;

import android.os.AsyncTask;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SplashActivity;

/**
 * Created by Sudarshan on 12/12/2015.
 */
public class ConversationUpdate extends AsyncTask<SplashActivity, Void, Void> {
    private SplashActivity splashActivity;

    @Override
    protected Void doInBackground(SplashActivity... params) {
        this.splashActivity = params[0];
        DressCheckApplication.CMP.updateConversations();
        DressCheckApplication.CMP.updateUsers();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        splashActivity.updateCompleted();
    }

}
