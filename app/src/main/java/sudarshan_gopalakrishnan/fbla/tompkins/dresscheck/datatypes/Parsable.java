package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

/**
 *
 * The interface Parsable enables processes to call sendToParse(). This method is crucial in the data storage process and hence the
 * implementation of such an interface is crucial for the storage of the program.
 *
 * @author Sudarshan
 */
public interface Parsable {
    public void sendToParse();
    public void updateFromParse() throws com.parse.ParseException;
}
