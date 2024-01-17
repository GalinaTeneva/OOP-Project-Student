package bg.tu_varna.sit.oop1.interfaces;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

public interface CustomDeserializable<T> {
    T deserialize(String data) throws DeserializationException, StudentException, ProgramException, SubjectException;
}
