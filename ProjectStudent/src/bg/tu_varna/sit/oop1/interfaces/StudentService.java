package bg.tu_varna.sit.oop1.interfaces;

public interface StudentService {
    public void enroll(int facultyNumber, String programName, int group, String studentName);
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
