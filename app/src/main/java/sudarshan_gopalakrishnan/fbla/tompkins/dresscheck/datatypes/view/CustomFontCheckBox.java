package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.view;

/**
 * Created by Sudarshan on 2/3/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;

/**
 * The CustomFontTextView is a custom TextView that is used in the application as a replacement for the native TextView for the UI.
 * By using this particular TextView, the app is enabled to incorporate a custom font (from a third party vendor) upon initialization.
 *
 * Created by Sudarshan on 12/28/2015.
 */
public class CustomFontCheckBox extends CheckBox{
    public CustomFontCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFont();
    }

    public CustomFontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public CustomFontCheckBox(Context context) {
        super(context);
        initFont();
    }


    /**
     * The method initFont() is called upon the creation of the TextView. This allows the TextView to use the BebasNeue Regular font to display
     * text.
     */
    public void initFont() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/BebasNeue Regular.ttf");
        setTypeface(tf ,1);

        this.setTextColor(getResources().getColor(R.color.white));
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);

    }
}