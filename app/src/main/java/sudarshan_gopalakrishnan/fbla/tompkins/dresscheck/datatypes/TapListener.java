package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import android.view.View;

/**
 * NOTE: TapListener can be either used for single or double click only. Do not use both of the following methods together: onSingleClick
 *  onDoubleClick
 *
 *  The TapListener listens to the number of taps made by the user.
 */
public abstract class TapListener implements View.OnClickListener {

    private static final long DOUBLECLICK_DURATION = 300;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        /**
         * The below process checks whether the time difference between two clicks is less than 300 milliseconds: if yes, the process,
         * considers it as a double click, else it is treated as a single click.
         */

        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLECLICK_DURATION){
            onDoubleClick(v);
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);

}