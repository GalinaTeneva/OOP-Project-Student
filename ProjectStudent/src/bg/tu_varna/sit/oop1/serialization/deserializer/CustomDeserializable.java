package bg.tu_varna.sit.oop1.serialization.deserializer;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

/**
 * This interface defines the operation for custom deserialization of objects from a string.
 * It provides a method to deserialize a string into an object.
 */
public interface CustomDeserializable<T> {
    /**
     * Deserializes the given string into an object.
     * Throws an exception if there is an issue while deserializing the object.
     */
    T deserialize(String data) throws DeserializationException, StudentException, ProgramException, SubjectException;
}
