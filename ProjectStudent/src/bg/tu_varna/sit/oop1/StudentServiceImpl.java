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
    private HashSet<Student> students = new HashSet<Student>();

    public StudentServiceImpl(HashSet<Student> students) {
        setStudents(students);
    }

    public HashSet<Student> getStudents() {
        return students;
    }

    public void setStudents(HashSet<Student> students) {
        this.students = students;
    } //Consider using validation

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


    @Override
    public String serialize(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append(student.getName()).append(", ");
        sb.append(student.getFacultyNumber()).append(", ");
        sb.append(student.getCourse()).append(", ");
        sb.append(student.getProgram() != null ? student.getProgram().getName() : "null").append(", ");
        sb.append(student.getGroup()).append(", ");
        sb.append(student.getStatus().toString());

        if (student.getGradesBySubject() != null && !student.getGradesBySubject().isEmpty()) {
            sb.append(", ");
            StringBuilder gradesStringBuilder = new StringBuilder();

            for (Map.Entry<Subject, Double> entry : student.getGradesBySubject().entrySet()) {
                if (gradesStringBuilder.length() > 0) {
                    gradesStringBuilder.append("; ");
                }
                gradesStringBuilder.append(entry.getKey().getName()).append(" -> ").append(entry.getValue());
            }
            sb.append(gradesStringBuilder);
        }

        //Append average grade if there are subjects with grades!!

        return sb.toString();
    }

    @Override
    public Student deserialize(String line) throws DeserializationException {
        try {
            String[] parts = line.split(", ");

            if (parts.length < 6) {
                throw new StudentException("Invalid serialized student data");
            }

            String name = parts[0];
            int facultyNumber = Integer.parseInt(parts[1]);
            int course = Integer.parseInt(parts[2]);
            Program program = new Program(parts[3]);
            int group = Integer.parseInt(parts[4]);
            String status = parts[5];

            Student student = new Student(name, facultyNumber, program, course, group);
            student.setStatus(status);

            if (parts.length > 6) {
                Map<Subject, Double> gradesBySubject = new HashMap<Subject, Double>();
                String[] gradeParts = parts[6].split("; ");
                for (String gradePart : gradeParts) {
                    String[] gradeSplit = gradePart.split(" -> ");
                    Subject subject = new Subject(gradeSplit[0]);
                    Double grade = Double.valueOf(gradeSplit[1]);
                    gradesBySubject.put(subject, grade);
                }

                student.setGradesBySubject(gradesBySubject);
            }

            //If there sre subjects with grades:
            //double averageGrade = Double.parseDouble(parts[7]);
            //student.averageGrade = averageGrade;

            return student;
        } catch (StudentException | ProgramException | SubjectException e) {
            throw new DeserializationException("Error deserializing data", e);
        }
    }
}
