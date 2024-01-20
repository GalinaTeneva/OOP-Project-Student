package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.models.Program;
import bg.tu_varna.sit.oop1.models.Student;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

public class Execute {
    //TODO: Add comments explaining the logic in the class
    private String pathToProgramsDatabaseFile = "D:\\UserData\\Desktop\\ProgramsData.txt";
    private String pathToFileHelp = "D:\\UserData\\Desktop\\help.txt";

    private Scanner scanner;
    private StudentSerializer studentSerializer;
    private StudentDeserializer studentDeserializer;
    private StudentService studentService;
    private FileManager studentsFileManager;
    private Collection<Student> students;

    private Collection<Program> programs;
    private ProgramDeserializer programDeserializer;
    private FileManager programFileManager;

    private StudentReporter studentReporter;

    public Execute() {
        this.studentSerializer = new StudentSerializer();
        this.studentDeserializer = new StudentDeserializer();
        this.studentService = new StudentService();
        this.students = studentService.getStudents();
        this.programs = studentService.getPrograms();
        this.studentsFileManager = new FileManager(studentSerializer, studentDeserializer, students);
        this.studentReporter = new StudentReporter(students);
        this.scanner = new Scanner(System.in);

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

            if (command.equals("HELP")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileHelp))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("An error occurred while reading the file.");
                }

                continue;
            }

            try {
                if(command.equals("OPEN") && !isFileLoaded) {
                    necessaryCommandParts = 2;
                    if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                        boolean isDirExists = validateDirectory(commandParts[1]);
                        if(!isDirExists){
                            System.out.println("Invalid path");
                            continue;
                        }

                        filePath = commandParts[1];
                        fileName = getFileName(filePath);

                        studentsFileManager.open(filePath);
                        programFileManager.open(pathToProgramsDatabaseFile);
                    }

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
                        //report commands
                    case "PRINT":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentReporter.print(commandParts);
                        }
                        break;
                    case "PRINTALL":
                        necessaryCommandParts = 3;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentReporter.printAll(commandParts);
                        }
                        break;
                    case "PROTOCOL":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentReporter.protocol(commandParts);
                        }
                        break;
                    case "REPORT":
                        necessaryCommandParts = 2;
                        if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                            studentReporter.report(commandParts);
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private boolean validateDirectory(String path) {
        StringBuilder sb = new StringBuilder();
        String[] filePathParts = path.split("\\\\");

        for (int i = 0; i < filePathParts.length - 2; i++) {
            sb.append(filePathParts[i]).append("\\");
        }

        File directory = new File(sb.toString());

        // Check if the directory already exists
        if (/*directory.exists() && */directory.isDirectory()) {
            return true; // The directory exists
        } else {
            // Try to create the directory
            return false;
        }
    }

    private boolean checkCommandPartsLength(String[] parts, int count) {
        if (parts.length != count) {
            throw new IllegalArgumentException(UserMessages.WRONG_ARGUMENTS_COUNT.message);
        }

        return true;
    }

    private String getFileName(String path) {
        String[] filePathParts = path.split("\\\\");
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