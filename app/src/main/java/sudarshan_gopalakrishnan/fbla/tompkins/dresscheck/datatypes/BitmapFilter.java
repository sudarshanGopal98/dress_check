package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BitmapFilter {

	public static final int TOTAL_FILTER_NUM = 7;

	/**
	 * The method changeStyle applies a specific color filter to the given image, and returns a new copy of a bitmap.
     *
	 * @param bitmap The original Bitmap image that is to be altered.
	 * @param styleNo The ID of the requested filter.
     * @return Returns a new Bitmap based upon the requested style number.
	 */
	public static Bitmap changeStyle(Bitmap bitmap, int styleNo) {
		switch (styleNo) {
			case 1:
				return applyColorFilter(bitmap, Color.MAGENTA);
			case 2:
				return applyColorFilter(bitmap, Color.CYAN);
			case 3:
				return applyColorFilter(bitmap, Color.GREEN);
			case 4:
				return applyColorFilter(bitmap, Color.RED);
			case 5:
				return applyColorFilter(bitmap, Color.YELLOW);
			case 6:
				return applyColorFilter(bitmap, Color.LTGRAY);
			case 7:
				return applyColorFilter(bitmap, Color.BLUE);

			default: return bitmap;
		}
	}

	private static Bitmap applyColorFilter(Bitmap originalBitmap, int shadingColor) {
		int imageWidth = originalBitmap.getWidth();
		int imageHeight = originalBitmap.getHeight();
		int[] imagePixels = new int[imageWidth * imageHeight];
		originalBitmap.getPixels(imagePixels, 0, imageWidth, 0, 0, originalBitmap.getWidth(), imageHeight);

		for(int y = 0; y < imageHeight; ++y) {
			for(int x = 0; x < imageWidth; ++x) {
				int index = y * imageWidth + x;
				imagePixels[index] &= shadingColor;
			}
		}

		Bitmap toReturn = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_4444);
		toReturn.setPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
		return toReturn;
	}

}