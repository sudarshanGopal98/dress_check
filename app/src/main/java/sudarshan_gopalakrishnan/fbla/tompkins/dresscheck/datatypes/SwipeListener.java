package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import android.view.GestureDetector;
import android.view.MotionEvent;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Direction;

/**
 *
 * The SwipeListener is used with Android's GestureDetector to sense the swipes made by the user.
 *
 * Reference: http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down
 */
abstract public class SwipeListener extends GestureDetector.SimpleOnGestureListener{

    abstract public boolean onSwipe(Direction direction);

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Direction direction = getDirection(x1,y1,x2,y2);
        return onSwipe(direction);
    }

    /**
     * The method getDirection returns the direction led by the two points
     * @param x1 The x coordinate of the initial point of the swipe
     * @param y1 The y coordinate of the initial point of the swipe
     * @param x2 The x coordinate of the final point of the swipe
     * @param y2 The y coordinate of the final point of the swipe
     * @return Returns the direction of swipe
     */
    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.get(angle);
    }

    /**
     * The method getAngle returns the angle formed by the two points
     * @param x1 The x coordinate of the initial point of the swipe
     * @param y1 The y coordinate of the initial point of the swipe
     * @param x2 The x coordinate of the final point of the swipe
     * @param y2 The y coordinate of the final point of the swipe
     * @return Returns the angle of swipe
     */
    private double getAngle(float x1, float y1, float x2, float y2) {
        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }



}