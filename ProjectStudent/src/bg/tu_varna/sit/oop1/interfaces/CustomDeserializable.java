package bg.tu_varna.sit.oop1.interfaces;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;

public interface CustomDeserializable<T> {
    T deserialize(String data) throws DeserializationException;
}
