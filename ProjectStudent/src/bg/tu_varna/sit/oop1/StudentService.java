package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.*;

public class StudentService implements Reportable {
    private HashSet<Student> students;

    public StudentService() {
        this.students = new HashSet<>();
    }

    public Collection<Student> getStudents() {
        return this.students;
    }

    //Reportable methods
    @Override
    public void print(String[] commandParts) {

    }

    @Override
    public void printAll(String[] commandParts) {

    }

    @Override
    public void protocol(String[] commandParts) {

    }

    @Override
    public void report(String[] commandParts) {

    }

    public void enroll (String[] commandParts) throws ProgramException, StudentException {
        //TODO: Check if the student exists and throw error if it doesn't!
        int facultyNumber = Integer.parseInt(commandParts[1]);
        //TODO: Check if the program exists and throw error if it doesn't!
        Program program = new Program(commandParts[2]);
        int group = Integer.parseInt(commandParts[3]);
        String studentName = commandParts[4];
        int year = 1;

        Student newStudent = new Student(studentName, facultyNumber, program, year, group);
        newStudent.setStatus("enrolled");
        students.add(newStudent);
    }

    public void advance(String[] commandParts) throws StudentException {
        int facultyNumber = Integer.parseInt(commandParts[1]);
        Student student = findStudentByFn(facultyNumber);

        if(student == null) {
            throw new StudentException("The student already exists!");
        }

        student.setYear(student.getYear() + 1);
    }

    public void change(String[] commandParts) {
        
    }

    public void graduate(int facultyNumber) {

    }

    public void interrupt(int facultyNumber) {

    }

    public void resume(int facultyNumber) {

    }

    public void enrollIn(int facultyNumber, String subjectName) {

    }

    public void addGrade(int facultyNumber, String subjectName, double grade) {

    }

    private Student findStudentByFn (int facultyNumber) {
        return  students.stream()
                .filter(std -> std.getFacultyNumber() == facultyNumber)
                .findFirst().orElse(null);
    }
}
