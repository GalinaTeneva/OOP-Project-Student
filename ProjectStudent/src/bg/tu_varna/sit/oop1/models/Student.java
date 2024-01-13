package bg.tu_varna.sit.oop1.models;

import bg.tu_varna.sit.oop1.Error;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.StudentStatus;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Student /*implements Serializable*/ {
    private String name;
    private int facultyNumber;
    private int course; //currCourse
    private Program program;
    private int group;
    private StudentStatus status;
    //private double averageGrade;

    private Map<Subject, Double> gradesBySubject = new HashMap<Subject, Double>();

    public Student (String name, int facultyNumber, Program program, int course, int group) throws StudentException {
        setName(name);
        setFacultyNumber(facultyNumber);
        setProgram(program);
        setCourse(course);
        setGroup(group);
    }

    //Get methods
    public String getName () {
        return this.name;
    }

    public int getFacultyNumber() {
        return this.facultyNumber;
    }

    public int getCourse () {
        return this.course;
    }

    public Program getProgram () {
        return this.program;
    }

    public int getGroup () {
        return this.group;
    }

    public StudentStatus getStatus () {
        return this.status;
    }

    public Map<Subject, Double> getGradesBySubject() {
        return this.gradesBySubject;
    }

    //Set methods
    public void setName (String name) throws StudentException {
        if (name == null || name.isEmpty()) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_NAME_NULL_VALUE.message);
        }

        this.name = name;
    }

    public void setFacultyNumber (Integer facultyNumber) throws StudentException {
        if (facultyNumber == null || facultyNumber == 0) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_FN_NULL_VALUE.message);
        }

        this.facultyNumber = facultyNumber;
    }

    public void setCourse (Integer course) throws StudentException {
        if (course == null || course == 0) {
            throw new StudentException(Error.STUDENT_COURSE_NULL_VALUE.message);
        }

        this.course = course;
    }

    public void setProgram (Program program) throws StudentException {
        if (program == null) {
            throw new StudentException(bg.tu_varna.sit.oop1.Error.STUDENT_PROGRAM_NULL_VALUE.message);
        }

        this.program = program;
    }

    public void setGroup (Integer group) throws StudentException {
        if (group == null || group == 0) {
            throw new StudentException(Error.STUDENT_GROUP_NULL_VALUE.message);
        }

        this.group = group;
    }

    public void setStatus (StudentStatus status) throws StudentException {
        //Write the validation!
        this.status = status;
    }

    public void setGradesBySubject (Map<Subject, Double> gradesBySubject) {
        //Write the validation!
        this.gradesBySubject = gradesBySubject;
    }

    public double calculateAverageGrade() {
        int gradesCount = gradesBySubject.size();
        Collection<Double> studentGrades = gradesBySubject.values();
        double gradesSum = 0;

        for(Double grade : studentGrades) {
            gradesSum += grade;
        }

        return gradesSum / gradesCount;
    }

}
