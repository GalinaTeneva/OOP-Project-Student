package bg.tu_varna.sit.oop1.serialization.deserializer;

import bg.tu_varna.sit.oop1.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.HashSet;

/**
 * The ProgramDeserializer class implements the CustomDeserializable interface for Program objects.
 * It provides a method to deserialize a string into a Program object.
 */
public class ProgramDeserializer implements CustomDeserializable<Program> {
    private HashSet<Program> programs;

    /**
     * Constructs a new ProgramDeserializer.
     * Initializes an empty set of programs.
     */
    public ProgramDeserializer() {
        this.programs = new HashSet<>();
    }

    /**
     * Deserializes the given string data into a Program object.
     * The data is has to be in a specific format.
     *
     * @param data The string that has to be deserialized.
     * @return A Program object deserialized from the string.
     * @throws DeserializationException If the string cannot be deserialized into a Program object due to missing data.
     * @throws ProgramException If the provided program data is with wrong format.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    @Override
    public Program deserialize(String data) throws DeserializationException, ProgramException, SubjectException {
        String[] parts = data.split(": ");

        if (parts.length < 2) {
            throw new DeserializationException(UserMessages.WRONG_PROGRAM_DATA_FORMAT.message);
        }

        String programName = parts[0];
        Program program = new Program(programName);
        getSubjectByCourse(parts[1], program);

        return program;
    }

    /**
     * Extracts and sets subjects for a specific year of study of a program.
     *
     * @param info The string containing subject information for a specific course.
     * @param program The Program object to which the extracted subjects will be added.
     * @throws DeserializationException If the string cannot be properly parsed into subjects due to missing data.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    private void getSubjectByCourse(String info, Program program) throws DeserializationException, SubjectException, ProgramException {
        String[] courseParts = info.split("; ");
        for (String coursePart : courseParts) {
            String[] courseSplit = coursePart.split(" -> ");
            if (courseSplit.length < 2) {
                throw new DeserializationException(UserMessages.WRONG_PROGRAM_DATA_FORMAT.message);
            }

            Integer courseNumber = Integer.parseInt(courseSplit[0]);

            String[] subjectsStrings = courseSplit[1].split(" \\| ");
            HashSet<Subject> subjects = getSubjectsCollection(subjectsStrings);

            program.setSubjectsByCourse(courseNumber, subjects);
        }
    }

    /**
     * Parses an array of subject strings into a collection of Subject objects.
     *
     * @param subjectsStrings An array of strings, each representing a subject.
     * @return A collection of Subject objects.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    private HashSet<Subject> getSubjectsCollection(String[] subjectsStrings) throws SubjectException {
        HashSet<Subject> subjects = new HashSet<>();
        for (String subjectStr : subjectsStrings) {
            Subject subject = getSubjectFromString(subjectStr);
            subjects.add(subject);
        }

        return subjects;
    }

    /**
     * Parses a string into a Subject object.
     *
     * @param subjectStr The string representing the subject.
     * @return A Subject object.
     * @throws SubjectException If the provided subject data is with wrong format.
     */
    private Subject getSubjectFromString(String subjectStr) throws SubjectException {
        String[] subjectInfo = subjectStr.split(" - ");
        String subjectName = subjectInfo[0];
        String subjectType = subjectInfo[1];
        return new Subject(subjectName, subjectType);
    }
}
