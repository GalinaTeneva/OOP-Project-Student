package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.interfaces.StudentService;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.*;

public class StudentServiceImpl implements StudentService {
    private HashSet<Student> students;

    public StudentServiceImpl() {
        this.students = new HashSet<>();
    }

    public Collection<Student> getStudents() {
        return this.students;
    }

    @Override
    public void enroll (String[] commandParts) throws ProgramException, StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Program program = new Program(commandParts[2]);
        int group = Integer.parseInt(commandParts[3]);
        String studentName = commandParts[4];
        int year = 1;

        Student newStudent = new Student(studentName, facultyNumber, program, year, group);
        newStudent.setStatus("enrolled");
        students.add(newStudent);
    }

    @Override
    public void advance(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst().orElse(null);
        if(student == null) {
            throw new StudentException("The student already exists!");
        }

        student.setYear(student.getYear() + 1);
    }

    @Override
    public void change(int facultyNumber, String option, String value) {

    }

    @Override
    public void graduate(int facultyNumber) {

    }

    @Override
    public void interrupt(int facultyNumber) {

    }

    @Override
    public void resume(int facultyNumber) {

    }

    @Override
    public void print(int facultyNumber) {

    }

    @Override
    public void printAll(String programName, int year) {

    }

    @Override
    public void enrollIn(int facultyNumber, String subjectName) {

    }

    @Override
    public void addGrade(int facultyNumber, String subjectName, double grade) {

    }

    @Override
    public void protocol(String subjectName) {

    }

    @Override
    public void report(int facultyNumber) {

    }
}
