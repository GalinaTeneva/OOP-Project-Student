package bg.tu_varna.sit.oop1.exceptions;

/**
 * The DeserializationException class represents an exception that is thrown
 * when an error occurs during the deserialization process.
 */
public class DeserializationException extends Exception {

    /**
     * Constructs a new DeserializationException with a given detail message.
     *
     * @param message The detail message.
     */
    public DeserializationException(String message) {
        super(message);
    }
}
