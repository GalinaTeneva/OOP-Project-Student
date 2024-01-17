package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.interfaces.CustomDeserializable;
import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.interfaces.StudentService;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;

public class StudentServiceImpl implements StudentService, CustomSerializable<Student>, CustomDeserializable<Student> {
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

    @Override
    public String serialize(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append(student.getName()).append(" | ");
        sb.append(student.getFacultyNumber()).append(" | ");
        sb.append(student.getProgram() != null ? student.getProgram().getName() : "null").append(" | ");
        sb.append(student.getYear()).append(" | ");
        sb.append(student.getGroup()).append(" | ");
        sb.append(student.getStatus().toString());

        if (student.getGradesBySubject() != null && !student.getGradesBySubject().isEmpty()) {
            sb.append(" | ");
            StringBuilder gradesStringBuilder = new StringBuilder();

            for (Map.Entry<Subject, Double> entry : student.getGradesBySubject().entrySet()) {
                if (gradesStringBuilder.length() > 0) {
                    gradesStringBuilder.append("; ");
                }
                gradesStringBuilder.append(entry.getKey().getName()).append(" -> ").append(entry.getValue());
            }
            sb.append(gradesStringBuilder);
        }

        return sb.toString();
    }

    @Override
    public Student deserialize(String line) throws DeserializationException {
        try {
            String[] parts = line.split(" \\| ");

            if (parts.length < 6) {
                throw new DeserializationException("Invalid serialized student data");
            }

            String name = parts[0];
            int facultyNumber = Integer.parseInt(parts[1]);
            Program program = new Program(parts[2]);
            int year = Integer.parseInt(parts[3]);
            int group = Integer.parseInt(parts[4]);
            String status = parts[5];

            Student student = new Student(name, facultyNumber, program, year, group);
            student.setStatus(status);

            if (parts.length > 6) {
                Map<Subject, Double> gradesBySubject = new HashMap<>();
                String[] gradeParts = parts[6].split("; ");
                for (String gradePart : gradeParts) {
                    String[] gradeSplit = gradePart.split(" -> ");
                    Subject subject = new Subject(gradeSplit[0]);
                    Double grade = Double.valueOf(gradeSplit[1]);
                    gradesBySubject.put(subject, grade);
                }

                student.setGradesBySubject(gradesBySubject);
            }

            return student;
        } catch (StudentException | ProgramException | SubjectException e) {
            throw new DeserializationException("Error deserializing data", e);
        }
    }

}
