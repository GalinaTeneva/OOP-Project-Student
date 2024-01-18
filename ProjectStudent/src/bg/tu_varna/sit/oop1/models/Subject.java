package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.Error;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

public class Subject {
    private String name;
    private String type;

    public Subject (String name, String type) throws SubjectException {
        setName(name);
        setType(type);
    }

    //Name get and set methods
    public String getName() {
        return this.name;
    }

    public void setName (String name) throws SubjectException {
        if (name == null || name.isEmpty()) {
            throw new SubjectException(Error.SUBJECT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    //Type get and set methods
    public String getType () {
        return  this.type;
    }

    public void setType (String type) throws SubjectException {
        if (!type.equalsIgnoreCase("mandatory") && !type.equalsIgnoreCase("optional")) {
            throw new SubjectException(Error.SUBJECT_TYPE_WRONG_VALUE.message);
        }

        this.type = type.toLowerCase();
    }
}
