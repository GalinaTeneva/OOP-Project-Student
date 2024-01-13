package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StudentServiceImpl implements StudentService{
    private HashSet<Student> students;

    public StudentServiceImpl(Collection<Student> students) {
        this.students = new HashSet<Student>(students);
    }

    public HashSet<Student> getStudents() {
        return students;
    }

    public void setStudents(HashSet<Student> students) {
        this.students = students;
    }

    @Override
    public void enroll() {

    }

    @Override
    public void advance() {

    }

    @Override
    public void change() {

    }

    @Override
    public void graduate() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void print() {

    }

    @Override
    public void printAll() {

    }

    @Override
    public void enrollIn() {

    }

    @Override
    public void addGrade() {

    }

    @Override
    public void protocol() {

    }

    @Override
    public void report() {

    }

    public void clearStudents() {
        this.students.clear();
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    // Additional methods for file serialization
    public String serializeStudent(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append(student.getName()).append(",");
        sb.append(student.getFacultyNumber()).append(",");
        sb.append(student.getCourse()).append(",");
        sb.append(student.getProgram() != null ? student.getProgram().getName() : "null").append(",");
        sb.append(student.getGroup()).append(",");
        sb.append(student.getStatus() != null ? student.getStatus().toString() : "null").append(",");

        if (student.getGradesBySubject() != null && !student.getGradesBySubject().isEmpty()) {
            StringBuilder gradesStringBuilder = new StringBuilder();
            for (Map.Entry<Subject, Double> entry : student.getGradesBySubject().entrySet()) {
                if (gradesStringBuilder.length() > 0) {
                    gradesStringBuilder.append(";");
                }
                gradesStringBuilder.append(entry.getKey().toString()).append(":").append(entry.getValue());
            }
            sb.append(",").append(gradesStringBuilder.toString());
        }

        return sb.toString();
    }

    public Student deserializeStudent(String line) throws StudentException, ProgramException, SubjectException {
        String[] parts = line.split(",");

        if (parts.length < 6) {
            throw new StudentException("Invalid serialized student data");
        }

        String name = parts[0];
        int facultyNumber = Integer.parseInt(parts[1]);
        int course = Integer.parseInt(parts[2]);
        Program program = new Program(parts[3]);
        int group = Integer.parseInt(parts[4]);
        StudentStatus status = StudentStatus.valueOf(parts[5]);
        //double averageGrade = Double.parseDouble(parts[6]);

        Student student = new Student(name, facultyNumber, program, course, group);
        student.setStatus(status);
        //student.averageGrade = averageGrade;

        if (parts.length > 6) {
            Map<Subject, Double> gradesBySubject = new HashMap<Subject, Double>();
            String[] gradeParts = parts[7].split(";");
            for (String gradePart : gradeParts) {
                String[] gradeSplit = gradePart.split(":");
                Subject subject = new Subject(gradeSplit[0]);
                Double grade = Double.valueOf(gradeSplit[1]);
                gradesBySubject.put(subject, grade);
            }

            student.setGradesBySubject(gradesBySubject);
        }

        return student;
    }
}
