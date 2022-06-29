package bms.exceptions;

/**
 * Exception thrown when a save file containing a list of building data is invalid.
 */
public class FileFormatException extends Exception {
    /**
     * Constructs a FileFormatException that contains a helpful message
     * detailing why the exception occurred.
     */
    public FileFormatException() {
        super();
    }
}
