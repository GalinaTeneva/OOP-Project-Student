package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.Collection;
import java.util.Scanner;

public class Execute {
    //TODO:Add method help!

    private String pathToProgramsDatabaseFile = "D:\\UserData\\Desktop\\ProgramsData.txt";

    private Scanner scanner;
    private StudentSerializer studentSerializer;
    private StudentDeserializer studentDeserializer;
    private StudentService studentService;
    private FileManager studentsFileManager;
    private Collection<Student> students;

    private Collection<Program> programs;
    private ProgramDeserializer programDeserializer;
    private FileManager programFileManager;

    public Execute() {
        this.studentSerializer = new StudentSerializer();
        this.studentDeserializer = new StudentDeserializer();
        this.studentService = new StudentService();
        this.students = studentService.getStudents();
        this.programs = studentService.getPrograms();
        this.studentsFileManager = new FileManager(studentSerializer, studentDeserializer, students);
        this.scanner = scanner = new Scanner(System.in);

        this.programDeserializer = new ProgramDeserializer();
        this.programFileManager = new FileManager(programDeserializer, programs);
    }

    public void runProject() {
        System.out.println("WELCOME");
        System.out.println("This is a student management system");

        while (true) {
            System.out.print("Enter command: ");
            String commandLine = scanner.nextLine();
            String[] commandParts = commandLine.split(" ");
            String command = commandParts[0];

            try {
                switch (command.toLowerCase()) {
                    //general commands
                    case "open":
                        studentsFileManager.open(commandParts[1]);
                        programFileManager.open(pathToProgramsDatabaseFile);
                        break;
                    case "close":
                        studentsFileManager.close();
                        break;
                    case "save":
                        studentsFileManager.save();
                        break;
                    case "saveas":
                        studentsFileManager.saveAs(commandParts[1]);
                        break;
                    case "exit":
                        return;
                    //StudentServiceCommands
                    case "enroll":
                        studentService.enroll(commandParts);
                        break;
                    case "advance":
                        studentService.advance(commandParts);
                        break;
                    case "change":
                        studentService.change(commandParts);
                        break;
                    case "graduate":
                        studentService.graduate(commandParts);
                        break;
                    case "interrupt":
                        studentService.interrupt(commandParts);
                        break;
                    case "resume":
                        studentService.resume(commandParts);
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
