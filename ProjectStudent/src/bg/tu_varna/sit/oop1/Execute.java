package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.util.Collection;
import java.util.HashSet;
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
        boolean isFileLoaded = false;
        String fileName = "";
        String filePath = "";
        int necessaryCommandParts;

        System.out.println(UserMessages.GREETING.message);

        while (true) {
            System.out.print(UserMessages.ENTER_COMMAND.message);
            String commandLine = scanner.nextLine();
            String[] commandParts = commandLine.split(" ");

            String command = commandParts[0].toUpperCase();

            //Checking if the given command is valid
            HashSet<String> validCommands = getCommands();
            if (!validCommands.contains(command)) {
                System.out.println(UserMessages.COMMAND_UNKNOWN.message);
                continue;
            }

            if(command.equals("EXIT")) {
                System.out.println(UserMessages.EXIT.message);
                return;
            }

            try {
                if(command.equals("OPEN") && !isFileLoaded) {
                    necessaryCommandParts = 2;
                    if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                        studentsFileManager.open(commandParts[1]);
                        programFileManager.open(pathToProgramsDatabaseFile);
                    }

                    filePath = commandParts[1];
                    fileName = getFileName(filePath);
                    System.out.println("Successfully opened " + fileName);
                    isFileLoaded = true;
                    continue;
                }

                if(!isFileLoaded) {
                    throw new Exception(UserMessages.FILE_NOT_LOADED.message);
                }

                switch (command) {
                    //general commands
                    case "OPEN":
                        System.out.println(fileName + " is already opened.");
                        break;
                    case "CLOSE":
                        necessaryCommandParts = 1;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentsFileManager.close();
                            isFileLoaded = false;
                            System.out.println("Successfully closed " + fileName);
                        }
                        break;
                    case "SAVE":
                        necessaryCommandParts = 1;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentsFileManager.save(filePath);
                            System.out.println("Successfully saved " + fileName);
                        }
                        break;
                    case "SAVEAS":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            String newPath = commandParts[1];
                            String anotherFileName = getFileName(newPath);
                            studentsFileManager.saveAs(newPath);
                            System.out.println("Successfully saved another " + anotherFileName);
                        }
                        break;
                    //StudentServiceCommands
                    case "ENROLL":
                        necessaryCommandParts = 5;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.enroll(commandParts);
                        }
                        break;
                    case "ADVANCE":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.advance(commandParts);
                        }
                        break;
                    case "CHANGE":
                        necessaryCommandParts = 4;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.change(commandParts);
                        }
                        break;
                    case "GRADUATE":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.graduate(commandParts);
                        }
                        break;
                    case "INTERRUPT":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.interrupt(commandParts);
                        }
                        break;
                    case "RESUME":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.resume(commandParts);
                        }
                        break;
                    case "ENROLLIN":
                        necessaryCommandParts = 3;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.enrollIn(commandParts);
                        }
                        break;
                    case "ADDGRADE":
                        necessaryCommandParts = 4;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentService.addGrade(commandParts);
                        }
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

    public HashSet<String> getCommands () {
        Command[] commands = Command.values();
        HashSet<String> commandSet = new HashSet<>();

        for (Command command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

}