package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.interfaces.CustomDeserializable;
import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class ProgramService implements CustomSerializable<Program>, CustomDeserializable<Program> {
    private HashSet<Program> programs;

    public ProgramService(HashSet<Program> programs) {
        setPrograms(programs);
    }

    public HashSet<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(HashSet<Program> programs) {
        this.programs = programs;
    } //Consider using validation

    @Override
    public String serialize(Program program) {
        StringBuilder sb = new StringBuilder();

        sb.append(program.getName());

        if (program.getSubjectsByCourse() != null && !program.getSubjectsByCourse().isEmpty()) {
            sb.append(": ");
            StringBuilder coursesStringBuilder = new StringBuilder();

            for (Map.Entry<Integer, Collection<Subject>> entry : program.getSubjectsByCourse().entrySet()) {
                if (coursesStringBuilder.length() > 0) {
                    coursesStringBuilder.append("; "); // Separator between different courses
                }
                coursesStringBuilder.append(entry.getKey()).append(" -> "); // Append course number

                StringBuilder subjectsStringBuilder = new StringBuilder();
                for (Subject subject : entry.getValue()) {
                    if (subjectsStringBuilder.length() > 0) {
                        subjectsStringBuilder.append(" | "); // Separator between different subjects in the same course
                    }
                    subjectsStringBuilder.append(subject.getName()).append(" - ");
                    subjectsStringBuilder.append(subject.getType());
                }

                coursesStringBuilder.append(subjectsStringBuilder);
            }

            sb.append(coursesStringBuilder);
        }

        return sb.toString();
    }

    @Override
    public Program deserialize(String data) throws DeserializationException {
        try {
            String[] parts = data.split(": ");

            if (parts.length < 2) {
                throw new DeserializationException("Invalid data format for Program");
            }

            String programName = parts[0];
            Program program = new Program(programName);

            String[] courseParts = parts[1].split("; ");
            for (String coursePart : courseParts) {
                String[] courseSplit = coursePart.split(" -> ");
                if (courseSplit.length < 2) {
                    throw new DeserializationException("Invalid course data format");
                }

                Integer courseNumber = Integer.parseInt(courseSplit[0]);
                String[] subjectParts = courseSplit[1].split(" \\| ");

                HashSet<Subject> subjects = new HashSet<>();
                for (String subjectStr : subjectParts) {
                    Subject subject = getSubjectFromString(subjectStr);
                    subjects.add(subject);
                }

                program.setSubjectsByCourse(courseNumber, subjects);
            }

            return program;
        } catch (Exception e) {
            throw new DeserializationException("Failed to deserialize Program: " + e.getMessage(), e);
        }
    }

    private Subject getSubjectFromString(String subjectStr) throws SubjectException {
        String[] subjectInfo = subjectStr.split(" - ");
        String subjectName = subjectInfo[0];
        String subjectType = subjectInfo[1];
        return new Subject(subjectName, subjectType);
    }
}
