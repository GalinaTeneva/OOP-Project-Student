package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

/**
 * The Subject class represents a subject with attributes name and type.
 */
public class Subject {
    private String name;
    private String type;

    /**
     * Constructs a new Subject with the specified name and type.
     *
     * @param name The name of the subject
     * @param type The type of the subject
     * @throws SubjectException if any parameter validation fails.
     */
    public Subject (String name, String type) throws SubjectException {
        setName(name);
        setType(type);
    }

    /**
     * Gets the name of the subject.
     *
     * @return The name of the subject.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the subject.
     *
     * @param name The name to set.
     * @throws SubjectException if the name is null or empty.
     */
    public void setName (String name) throws SubjectException {
        if (name == null || name.isEmpty()) {
            throw new SubjectException(UserMessages.SUBJECT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    /**
     * Gets the type of the subject.
     *
     * @return The type of the subject.
     */
    public String getType () {
        return  this.type;
    }

    /**
     * Sets the type of the subject.
     *
     * @param type The type to set.
     * @throws SubjectException if the type is not "mandatory" or "optional".
     */
    public void setType (String type) throws SubjectException {
        if (!type.equalsIgnoreCase("mandatory") && !type.equalsIgnoreCase("optional")) {
            throw new SubjectException(UserMessages.SUBJECT_TYPE_WRONG_VALUE.message);
        }

        this.type = type.toLowerCase();
    }
}
