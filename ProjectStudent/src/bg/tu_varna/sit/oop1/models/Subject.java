package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.Error;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

import java.io.Serializable;

public class Subject implements Serializable {
    private String name;
    private String type;

    public Subject (String name, String type) throws SubjectException {
        setName(name);
        setType(type);
    }

    //Get methods

    public String getName() { return this.name; }

    public String getType ()
    {
        return  this.type;
    }

    //Set Methods

    public void setName (String name) throws SubjectException {
        if (name == null || name.isEmpty()) {
            throw new SubjectException(Error.SUBJECT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    public void setType (String type) throws SubjectException {
        if (!type.equalsIgnoreCase("mandatory") && !type.equalsIgnoreCase("optional")) {
            throw new SubjectException(Error.SUBJECT_TYPE_WRONG_VALUE.message);
        }

        this.type = type.toLowerCase();
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
