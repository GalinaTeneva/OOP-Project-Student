package bg.tu_varna.sit.oop1.serialization.deserializer;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.HashMap;
import java.util.Map;

/**
 * The StudentDeserializer class implements the CustomDeserializable interface for Student objects.
 * It provides a method to deserialize a string into a Student object.
 */
public class StudentDeserializer implements CustomDeserializable<Student> {

    /**
     * Deserializes the given string line into a Student object.
     * The data is has to be in a specific format.
     *
     * @param data The string that has to be deserialized.
     * @return A Student object deserialized from the string.
     * @throws DeserializationException If the string cannot be deserialized into a Student object due to a missing data.
     * @throws StudentException If the provided student data is with wrong format.
     * @throws ProgramException If the provided program data is with wrong format.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    public Student deserialize(String data) throws DeserializationException, StudentException, ProgramException, SubjectException {
        String[] parts = data.split(" \\| ");

        if (parts.length < 6) {
            throw new DeserializationException(UserMessages.WRONG_STUDENT_DATA_FORMAT.message);
        }

        String name = parts[0].split(": ")[1];
        int facultyNumber = Integer.parseInt(parts[1].split(": ")[1]);
        Program program = new Program(parts[2].split(": ")[1]);
        int year = Integer.parseInt(parts[3].split(": ")[1]);
        int group = Integer.parseInt(parts[4].split(": ")[1]);
        String status = parts[5].split(": ")[1];

        Student student = new Student(facultyNumber, name, program, year, group);
        student.setStatus(status);

        if (parts.length > 6) {
            String[] gradeParts = parts[6].split(": ");

            Map<Subject, Double> gradesBySubject = getGradesBySubject(gradeParts[1]);

            student.setGradesBySubject(gradesBySubject);
        }

        return student;
    }

    /**
     * Parses a string containing grades information into a map of Subject linked to Double.
     * Double represents the value of a grade.
     *
     * @param gradesString The string containing the grade information.
     * @return A map linking each Subject with its corresponding grade.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    private Map<Subject, Double> getGradesBySubject(String gradesString) throws SubjectException {
        Map<Subject, Double> gradesBySubject = new HashMap<>();

        String[] allGrades = gradesString.split("; ");
        for (String gradeInfo : allGrades) {
            String[] gradeSplit = gradeInfo.split(" -> ");
            String subjectName = gradeSplit[0];
            String subjectType = gradeSplit[1];
            Subject subject = new Subject(subjectName, subjectType);
            Double grade = Double.valueOf(gradeSplit[2]);
            gradesBySubject.put(subject, grade);
        }

        return gradesBySubject;
    }
}
