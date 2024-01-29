package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;

import java.util.*;

/**
 * The Program class represents a program with attribute name.
 * It also has a record of all subjects for a given year of study.
 */
public class Program {
    private String name;
    private Map<Integer, Collection<Subject>> subjectsByCourse = new HashMap<>();

    /**
     * Constructs a new Program with the specified name.
     *
     * @param name The name of the program.
     * @throws ProgramException if any parameter validation fails.
     */
    public Program (String name) throws ProgramException {
        setName(name);
    }

    /**
     * Gets the name of the program.
     *
     * @return The name of the program.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the program.
     *
     * @param name The name to set.
     * @throws ProgramException if the name is null or empty.
     */
    public void setName(String name) throws ProgramException {
        if(name == null || name.isEmpty()) {
            throw new ProgramException(UserMessages.PROGRAM_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    /**
     * Gets the map of the subjects by year of study for the program.
     *
     * @return Map containing a collection of subjects for each year of study.
     */
    public Map<Integer, Collection<Subject>> getSubjectsByCourse ()
    {
        return this.subjectsByCourse;
    }

    /**
     * Sets the subjects for a specific year of study of a program.
     *
     * @param course The course number for which the subjects are to be set.
     * @param courseSubjects The collection of subjects to be associated with the specified year of study.
     * @throws ProgramException If the course number is not in the valid range (1-4) or if the collection of course subjects is null or empty.
     */
    public void setSubjectsByCourse (Integer course, Collection<Subject> courseSubjects) throws ProgramException {

        if(course <= 0 || course > 4 ) {
            throw new ProgramException(UserMessages.PROGRAM_COURSE_WRONG_VALUE.message);
        }
        if (courseSubjects == null || courseSubjects.isEmpty()) {
            throw new ProgramException(UserMessages.PROGRAM_COURSE_SUBJECTS_VALUE.message);
        }

        this.subjectsByCourse.put(course, courseSubjects);
    }
}
