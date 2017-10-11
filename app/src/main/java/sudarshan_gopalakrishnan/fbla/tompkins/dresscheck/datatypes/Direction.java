package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

/**
 * enum Direction is a data type that is used with respect to swipe direction detection.
 * The angles are defined as follows:
     * Up: [45, 135]
     * Right: [0,45] and [315, 360]
     * Down: [225, 315]
     * Left: [135, 225]
 *
 * Reference: http://stackoverflow.com/questions/13095494/how-to-detect-swipe-direction-between-left-right-and-up-down
 */
public enum Direction{
    up,
    down,
    left,
    right;


    /**
     *
     *
     * @param angle double angle is an angle that ranges from 0 to 360 degrees
     * @return Returns a direction given an angle.
     */
    public static Direction get(double angle){
        if(isInRange(angle, 45, 135)){
            return Direction.up;
        }
        else if(isInRange(angle, 0, 45) || isInRange(angle, 315, 360)){
            return Direction.right;
        }
        else if(isInRange(angle, 225, 315)){
            return Direction.down;
        }
        else{
            return Direction.left;
        }

    }

    /**
     * @param angle an angle
     * @param init the initial bound
     * @param end the final bound
     * @return returns true if the given angle is in the interval [initFont, end).
     */
    private static boolean isInRange(double angle, float init, float end){
        return (angle >= init) && (angle < end);
    }
}
