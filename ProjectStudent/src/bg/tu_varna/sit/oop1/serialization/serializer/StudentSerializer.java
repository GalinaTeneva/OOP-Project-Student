package bg.tu_varna.sit.oop1.serialization.serializer;

import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Map;

/**
 * This class implements the CustomSerializable interface for Student objects.
 * It provides a method for serialization a Student object into a string.
 */
public class StudentSerializer implements CustomSerializable<Student>{

    /**
     * Serializes the given Student object into a string.
     * The serialized string includes the student's name, faculty number, program, year, group, student status.
     * Also includes grades if there are any.
     *
     * @param student The Student object to be serialized.
     * @return A string representing the Student object.
     */
    @Override
    public String serialize(Student student) {
        StringBuilder sb = new StringBuilder();

        sb.append("Name: ").append(student.getName()).append(" | ");
        sb.append("Faculty number: ").append(student.getFacultyNumber()).append(" | ");
        sb.append("Program: ").append(student.getProgram().getName()).append(" | ");
        sb.append("Year: ").append(student.getYear()).append(" | ");
        sb.append("Group: ").append(student.getGroup()).append(" | ");
        sb.append("Status: ").append(student.getStatus().toString());

        if (student.getGradesBySubject() != null && !student.getGradesBySubject().isEmpty()) {
            sb.append(" | ");
            sb.append("Grades: ");

            String studentGradesString = getGradesString(student);

            sb.append(studentGradesString);
        }

        return sb.toString();
    }

    /**
     * Helper method to create a string of the student's grades.
     *
     * @param student The Student object whose grades must be serialized.
     * @return A string representing the student's grades.
     */
    private String getGradesString(Student student) {
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

        return gradesStringBuilder.toString();
    }
}
