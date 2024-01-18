package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Map;

public class StudentSerializer implements CustomSerializable<Student>{

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
                gradesStringBuilder
                        .append(entry.getKey().getName())
                        .append(" -> ")
                        .append(entry.getKey().getType())
                        .append(" -> ")
                        .append(entry.getValue());
            }
            sb.append(gradesStringBuilder);
        }

        return sb.toString();
    }
}
