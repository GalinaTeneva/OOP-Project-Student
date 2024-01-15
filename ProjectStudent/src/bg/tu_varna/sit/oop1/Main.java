package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.StudentService;
import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;
import bg.tu_varna.sit.oop1.models.Subject;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        HashSet<Program> programs = new HashSet<>();
        HashSet<Student> students = new HashSet<>();
        ProgramServices programService = new ProgramServices(programs);
        StudentServiceImpl studentService = new StudentServiceImpl(students);

        FileManager fileManager = new FileManager(studentService);

        try {
            Subject subject1 = new Subject("OOP", "maNdatory");
            Subject subject2 = new Subject("Subject2", "optional");
            Subject subject3 = new Subject("Subject3", "maNdatory");
            Subject subject4 = new Subject("Subject4", "optional");
            Subject subject5 = new Subject("Subject5", "mandatory");
            Subject subject6 = new Subject("Subject6", "optional");
            Subject subject7 = new Subject("Subject7", "maNdatory");

            Program program1 = new Program("Program1");
            Program program2 = new Program("Program1");

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

            Student student1 = new Student("Vasil", 1213, program1, 2, 2);
            Student student2 = new Student("Kamen", 1415, program2, 1, 3);

            students.add(student1);
            students.add(student2);

            Map<Subject, Double> std1GradesBySbj = new HashMap<Subject, Double>();
            std1GradesBySbj.put(subject1, 5.50);
            std1GradesBySbj.put(subject2, 3.70);
            std1GradesBySbj.put(subject3, 4.80);

            student1.setGradesBySubject(std1GradesBySbj);

            student1.setStatus("ENrolled");
            student2.setStatus("enrolled");

            String student1Data = studentService.serialize(student1);
            String student2Data = studentService.serialize(student2);

            System.out.println(student1Data);
            System.out.println(student2Data);

            fileManager.saveAs("D:\\UserData\\Desktop\\test.txt");
            System.out.println("The data read from the file is:");
            fileManager.open("D:\\UserData\\Desktop\\test.txt");
            System.out.println("End");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
