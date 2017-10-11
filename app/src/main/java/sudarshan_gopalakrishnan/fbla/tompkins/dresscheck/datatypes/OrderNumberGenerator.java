package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import java.util.ArrayList;

/**
 * The class OrderNumberGenerator is used to generate a sequence of numbers, which are used to create a looping sequence of lists.
 * @author Sudarshan
 */
public class OrderNumberGenerator {
    private int limit;
    private int currentNumber;

    /**
     * Initializes the class by setting the limit of the sequence
     * @param limit The limit of the list
     */
    public OrderNumberGenerator(int limit){
        this.limit = limit;
        this.currentNumber = 0;
    }

    /**
     * The method returns the next number in the sequence. If the parameter is true, then the sequence will take in an ascending
     * order, else it will take a descending sequence.
     *
     * @param increase The parameter determines if the sequence is ascending or descending
     * @return Returns the next number in the sequence
     */
    public int next(boolean increase) {
        if (increase) {
            currentNumber = ++currentNumber;
            if(currentNumber == limit){
                currentNumber = 0;
            }
            return currentNumber;
        }else{
            currentNumber = --currentNumber;
            if(currentNumber < 0){
                currentNumber = limit - 1;
            }
            return currentNumber;
        }
    }
}
