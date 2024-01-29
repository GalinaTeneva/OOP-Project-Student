package bg.tu_varna.sit.oop1.serialization.serializer;

/**
 * This interface defines the operation for custom serialization of objects.
 * It provides a method to serialize an object into a string.
 */
public interface CustomSerializable<T> {
    /**
     * Serializes the given object into a string.
     * Returns a string representing the object.
     */
    String serialize(T object);
}
