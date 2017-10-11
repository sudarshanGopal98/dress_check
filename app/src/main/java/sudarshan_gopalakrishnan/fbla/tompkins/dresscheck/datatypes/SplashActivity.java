package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

/**
 * The interface SplashActivity is to be implemented by all activities that serve as splash screens where information
 * is being updated from the server.
 */
public interface SplashActivity {
    public void updateCompleted();
    public void updateCompleted(Object resultObject);

}
