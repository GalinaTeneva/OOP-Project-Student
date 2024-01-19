package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.Collection;
import java.util.Scanner;

public class Execute {
    //TODO:Add method help!
    //TODO: Add the confirming messages after each method!

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
        boolean isFileLoaded = false;
        String fileName = "";
        int necessaryCommandParts;

        System.out.println(UserMessages.GREETING.message);

        while (true) {
            System.out.print(UserMessages.ENTER_COMMAND.message);
            String commandLine = scanner.nextLine();
            String[] commandParts = commandLine.split(" ");

            String command = commandParts[0].toLowerCase();

            if(command.equals("exit")) {
                System.out.println(UserMessages.EXIT.message);
                return;
            }

            try {
            if(command.equals("open") && !isFileLoaded) {
                necessaryCommandParts = 2;
                if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                    studentsFileManager.open(commandParts[1]);
                    programFileManager.open(pathToProgramsDatabaseFile);
                }

                fileName = getFileName(commandParts[1]);
                System.out.println("Successfully opened " + fileName);
                isFileLoaded = true;
                continue;
            }

            if(!isFileLoaded) {
                throw new Exception(UserMessages.FILE_NOT_LOADED.message);
            }

                switch (command) {
                    //general commands
                    case "open":
                            System.out.println(fileName + " is already opened.");
                        break;
                    case "close":
                        necessaryCommandParts = 1;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentsFileManager.close();
                            isFileLoaded = false;
                            System.out.println("Successfully closed " + fileName);
                        }
                        break;
                    case "save":
                        necessaryCommandParts = 1;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentsFileManager.save();
                            System.out.println("Successfully saved " + fileName);
                        }
                        break;
                    case "saveas":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentsFileManager.saveAs(commandParts[1]);
                            String anotherFileName = getFileName(commandParts[1]);
                            System.out.println("Successfully saved another " + anotherFileName);
                        }
                        break;
                    //StudentServiceCommands
                    case "enroll":
                        necessaryCommandParts = 5;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.enroll(commandParts);
                        }
                        break;
                    case "advance":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.advance(commandParts);
                        }
                        break;
                    case "change":
                        necessaryCommandParts = 4;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.change(commandParts);
                        }
                        break;
                    case "graduate":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.graduate(commandParts);
                        }
                        break;
                    case "interrupt":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.interrupt(commandParts);
                        }
                        break;
                    case "resume":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.resume(commandParts);
                        }
                        break;
                    case "enrollin":
                        necessaryCommandParts = 3;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.enrollIn(commandParts);
                        }
                        break;
                    case "addgrade":
                        necessaryCommandParts = 4;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.addGrade(commandParts);
                        }
                        break;
                    default:
                        System.out.println(UserMessages.COMMAND_UNKNOWN.message);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    private boolean checkCommandPartsLength(String[] parts, int count) {
        if (parts.length != count) {
            throw new IllegalArgumentException(UserMessages.WRONG_ARGUMENTS_COUNT.message);
        }

        return true;
    }

    private String getFileName(String text) {
        String[] filePathParts = text.split("\\\\");
        String fileName = filePathParts[filePathParts.length - 1];
        return  fileName;
    }
}
