package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;

/**
 * BitmapHelperTools is used to manipulate a particular bitmap into various forms as needed.
 */
public class BitmapHelperTools{

    /**
     * Method rotateBitmap rotates the original bitmap to the angle provided as parameter.
     * @param source The original Bitmap image
     * @param angle The angle of rotation
     * @return Returns a image that is rotated to the angle specified by the parameter
     */
    public static Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Flips the image either horizontally or vertically.
     * @param source The original Bitmap image
     * @param horizontal A boolean that specifies if the image has to be flipped horizontally
     * @return Returns a Bitmap that is flipped either horizontally or vertically
     */
    public static Bitmap flipBitmap(Bitmap source, boolean horizontal) {
        Matrix matrix = new Matrix();

        if(horizontal)
            matrix.preScale(1.0f, -1.0f);
        else
            matrix.preScale(-1.0f, 1.0f);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Converts a given Bitmap to a byte[] in order to save memory during the storage process.
     * @param source The original Bitmap
     * @return Returns a byte[] in which data about the source image is stored
     */
    public static byte[] convertToByteArray(Bitmap source){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return  byteArray;
    }

    /**
     * Converts a given byte[] containing data about a Bitmap into a Bitmap image
     * @param bitmapdata byte[] bitmapdata is the data needed to create a Bitmap
     * @return Returns a Bitmap based upon the data in bitmapdata.
     */
    public static Bitmap convertToBitmap(byte[] bitmapdata){
        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    /**
     * Scales the Bitmap to the size specified by the parameters.
     * @param source The original Bitmap image.
     * @param newSize The new width or height to which the image is to be scaled
     * @return Returns a scaled Bitmap, which is scaled upon the ratio of width and height.
     */
    public static Bitmap getResizedBitmap(Bitmap source, int newSize) {
        int width = source.getWidth();
        int height = source.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = newSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = newSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(source, width, height, true);
    }

}
