package bg.tu_varna.sit.oop1.interfaces;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;

public interface StudentService {
    public void enroll(String[] commandParts) throws ProgramException, StudentException;
    public void advance(int facultyNumber);
    public void change(int facultyNumber, String option, String value);
    public void graduate(int facultyNumber);
    public void interrupt(int facultyNumber);
    public void resume(int facultyNumber);
    public void print(int facultyNumber);
    public void printAll(String programName, int year);
    public void enrollIn(int facultyNumber, String subjectName);
    public void addGrade(int facultyNumber, String subjectName, double grade);
    public void protocol(String subjectName);
    public void report(int facultyNumber);
}
