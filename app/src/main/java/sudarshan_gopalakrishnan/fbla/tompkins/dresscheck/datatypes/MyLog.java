package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import android.util.Log;

/**
 * MyLog is used as a helper class to print data to the debugger console. Instead of repeatedly the Log class, the custom
 * MyLog allows the programmer and debugger to quickly make print commands, that are more similar to the System.out library.
 */
public class MyLog {

    public static final int GENERAL = 0;
    public static final int NETWORK = 1;
    public static final int ERROR = 2;


    public static final void print(String s, int type){
        switch(type) {
            case 0: Log.d("GENERAL:", s); break;
            case 1: Log.d("NETWORK:", s); break;
            case 2: Log.d("ERROR:", s); break;

        }
    }

    public static final void print(String s){
        Log.d("GENERAL:", s);
    }

    public static final void print(Exception e){
        Log.d("ERROR:", e.toString());
    }

    public static final void print(Object o){
    	if(o instanceof Exception){
    		print((Exception)o);
    	}
    	
        Log.d("GENERAL:", o.toString());
    }

}
