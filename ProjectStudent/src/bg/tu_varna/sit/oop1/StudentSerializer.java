package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Map;

public class StudentSerializer implements CustomSerializable<Student>{

    @Override
    public String serialize(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(student.getName()).append(" | ");
        sb.append("Faculty number: ").append(student.getFacultyNumber()).append(" | ");
        sb.append("Program: ").append(student.getProgram() != null ? student.getProgram().getName() : "null").append(" | ");
        sb.append("Year: ").append(student.getYear()).append(" | ");
        sb.append("Group: ").append(student.getGroup()).append(" | ");
        sb.append("Status: ").append(student.getStatus().toString());

        if (student.getGradesBySubject() != null && !student.getGradesBySubject().isEmpty()) {
            sb.append(" | ");
            sb.append("Grades: ");
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
