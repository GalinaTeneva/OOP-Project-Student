package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashSet<Program> programs = new HashSet<>();
        ProgramServices programService = new ProgramServices(programs);

        try {
            Subject subject1 = new Subject("OOP", "maNdatory");
            Subject subject2 = new Subject("Subject2", "optional");
            Subject subject3 = new Subject("Subject3", "maNdatory");
            Subject subject4 = new Subject("Subject4", "optional");
            Subject subject5 = new Subject("Subject5", "mandatory");
            Subject subject6 = new Subject("Subject6", "optional");
            Subject subject7 = new Subject("Subject7", "maNdatory");

            Program program1 = new Program("Program1");
            Program program2 = new Program("Program2");
            Program program3 = new Program("Program3");

            HashSet<Subject> program1Course1Sbjs = new HashSet<Subject>();
            program1Course1Sbjs.add(subject1);
            program1Course1Sbjs.add(subject2);
            program1Course1Sbjs.add(subject3);

            HashSet<Subject> program1Course2Sbjs = new HashSet<Subject>();
            program1Course2Sbjs.add(subject4);
            program1Course2Sbjs.add(subject5);
            program1Course2Sbjs.add(subject6);

            program1.setSubjectsByCourse(1, program1Course1Sbjs);
            program1.setSubjectsByCourse(2, program1Course2Sbjs);

            HashSet<Subject> program2Course2Sbjs = new HashSet<Subject>();
            program2Course2Sbjs.add(subject1);
            program2Course2Sbjs.add(subject2);
            program2Course2Sbjs.add(subject3);
            program2.setSubjectsByCourse(2, program2Course2Sbjs);

            programs.add(program1);
            programs.add(program2);
            programs.add(program3);

            for (Program program : programs) {
                String programString = programService.serialize(program);
                System.out.println(programString);
            }

            //Program program = programService.deserialize("Program1: 1 -> Subject3-mandatory|Subject2-optional|OOP-mandatory; 2 -> Subject5-mandatory|Subject4-optional|Subject6-optional");
            programService.deserialize("Program3");
            System.out.println("End");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
