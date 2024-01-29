package bg.tu_varna.sit.oop1.serialization.deserializer;

import bg.tu_varna.sit.oop1.UserMessages;
import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.HashSet;

public class ProgramDeserializer implements CustomDeserializable<Program> {
    private HashSet<Program> programs;

    public ProgramDeserializer() {
        this.programs = new HashSet<>();
    }

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

    private HashSet<Subject> getSubjectsCollection(String[] subjectsStrings) throws SubjectException {
        HashSet<Subject> subjects = new HashSet<>();
        for (String subjectStr : subjectsStrings) {
            Subject subject = getSubjectFromString(subjectStr);
            subjects.add(subject);
        }

        return subjects;
    }

    private Subject getSubjectFromString(String subjectStr) throws SubjectException {
        String[] subjectInfo = subjectStr.split(" - ");
        String subjectName = subjectInfo[0];
        String subjectType = subjectInfo[1];
        return new Subject(subjectName, subjectType);
    }
}
