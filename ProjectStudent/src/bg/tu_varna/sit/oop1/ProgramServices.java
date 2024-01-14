package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProgramServices implements CustomSerializable<Program> {
    private HashSet<Program> programs;

    public ProgramServices (HashSet<Program> programs) {
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
            sb.append(",");
            StringBuilder coursesStringBuilder = new StringBuilder();

            for (Map.Entry<Integer, Collection<Subject>> entry : program.getSubjectsByCourse().entrySet()) {
                if (coursesStringBuilder.length() > 0) {
                    coursesStringBuilder.append(";"); // Separator between different courses
                }
                coursesStringBuilder.append(entry.getKey()).append(":"); // Append course number

                StringBuilder subjectsStringBuilder = new StringBuilder();
                for (Subject subject : entry.getValue()) {
                    if (subjectsStringBuilder.length() > 0) {
                        subjectsStringBuilder.append("|"); // Separator between different subjects in the same course
                    }
                    subjectsStringBuilder.append(subject.toString()); // Assuming Subject has a meaningful toString implementation
                }

                coursesStringBuilder.append(subjectsStringBuilder);
            }

            sb.append(coursesStringBuilder.toString());
        }

        return sb.toString();
    }
}
