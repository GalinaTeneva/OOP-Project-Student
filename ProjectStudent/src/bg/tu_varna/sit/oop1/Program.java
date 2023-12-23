package bg.tu_varna.sit.oop1;

import java.util.*;

public class Program {

    private String name;
    private Map<Integer, Collection<Subject>> subjectsByCourse = new HashMap<>();

    public Program (String name)throws ProgramException {
        setName(name);
    }

    //Get methods

    public String getName() { return this.name; }

    public Map<Integer, Collection<Subject>> getSubjectsByCourse ()
    {
        return this.subjectsByCourse;
    }

    //Set methods

    public void setName(String name) throws ProgramException {
        if(name == null || name.isEmpty()) {
            throw new ProgramException(Error.PROGRAM_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    public void setSubjectsByCourse (Integer course, Collection<Subject> courseSubjects) throws ProgramException {

        if(course <= 0 || course > 4 ) {
            throw new ProgramException(Error.PROGRAM_COURSE_WRONG_VALUE.message);
        }
        if (courseSubjects == null || courseSubjects.isEmpty()) {
            throw new ProgramException(Error.PROGRAM_COURSE_SUBJECTS_VALUE.message);
        }

        this.subjectsByCourse.put(course, courseSubjects);
    }
}
