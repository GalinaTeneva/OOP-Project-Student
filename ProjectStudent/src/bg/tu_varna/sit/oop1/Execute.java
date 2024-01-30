package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.GeneralCommand;
import bg.tu_varna.sit.oop1.enums.ReportCommand;
import bg.tu_varna.sit.oop1.enums.StudentCommand;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.reporters.StudentReporter;
import bg.tu_varna.sit.oop1.serialization.deserializer.ProgramDeserializer;
import bg.tu_varna.sit.oop1.serialization.deserializer.StudentDeserializer;
import bg.tu_varna.sit.oop1.repositories.ProgramRepository;
import bg.tu_varna.sit.oop1.repositories.StudentRepository;
import bg.tu_varna.sit.oop1.serialization.serializer.StudentSerializer;
import bg.tu_varna.sit.oop1.services.StudentService;
import bg.tu_varna.sit.oop1.utilities.FileManager;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Execute {
    private String pathToProgramsDatabaseFile = ".\\ProgramsData.txt";
    private String pathToFileHelp = ".\\HelpInfo.txt";

    private Scanner scanner;
    private StudentSerializer studentSerializer;
    private StudentDeserializer studentDeserializer;
    private StudentService studentService;
    private FileManager studentsFileManager;

    private ProgramDeserializer programDeserializer;
    private FileManager programFileManager;

    private StudentReporter studentReporter;
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;

    public Execute() {
        this.studentRepository = new StudentRepository();
        this.programRepository = new ProgramRepository();

        this.studentSerializer = new StudentSerializer();
        this.studentDeserializer = new StudentDeserializer();
        this.studentService = new StudentService(studentRepository, programRepository);

        this.studentsFileManager = new FileManager(studentSerializer, studentDeserializer, studentRepository);
        this.studentReporter = new StudentReporter(studentRepository);
        this.scanner = new Scanner(System.in);

        this.programDeserializer = new ProgramDeserializer();
        this.programFileManager = new FileManager(programDeserializer, programRepository);
    }

    public void runProject() {
        boolean isFileLoaded = false;
        String fileName = "";
        String filePath = "";
        int necessaryCommandParts;

        System.out.println(UserMessages.GREETING.message);

        HashSet<String> validGeneralCommands = getGeneralCommands();
        HashSet<String> validReportCommands = getReportCommands();
        HashSet<String> validStudentCommands = getStudentCommands();

        while (true) {
            System.out.print(UserMessages.ENTER_COMMAND.message);
            String commandLine = scanner.nextLine();
            String[] commandParts = commandLine.split(" ");

            String command = commandParts[0].toUpperCase();

            //Checking if the given command is valid
            boolean isCommandValid = validGeneralCommands.contains(command)
                    || validReportCommands.contains(command)
                    || validStudentCommands.contains(command);
            if (!isCommandValid) {
                System.out.println(UserMessages.COMMAND_UNKNOWN.message);
                continue;
            }

            if(command.equals("EXIT")) {
                System.out.println(UserMessages.EXIT.message);
                return;
            }

            if (command.equals("HELP")) {
                String helpInfo = getHelpInfo(pathToFileHelp);
                System.out.println(helpInfo);
                continue;
            }

            try {
                if(command.equals("OPEN") && !isFileLoaded) {
                    necessaryCommandParts = 2;
                    if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
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

                if(validGeneralCommands.contains(command)){
                    switch (command) {
                        case "OPEN":
                            System.out.println(fileName + " is already opened.");
                            break;
                        case "CLOSE":
                            necessaryCommandParts = 1;
                            if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                                studentsFileManager.close();
                                programFileManager.close();
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
                                studentsFileManager.save(newPath);
                                System.out.println("Successfully saved another " + anotherFileName);
                            }
                            break;
                    }
                } else if (validStudentCommands.contains(command)) {
                    processStudentCommand(command, commandParts);
                } else if (validReportCommands.contains(command)) {
                    processProtocolCommand(command, commandParts);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String getHelpInfo(String pathToFileHelp) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFileHelp))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return sb.toString();
    }

    private HashSet<String> getStudentCommands() {
        StudentCommand[] commands = StudentCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (StudentCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    private HashSet<String> getReportCommands() {
        ReportCommand[] commands = ReportCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (ReportCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    private HashSet<String> getGeneralCommands() {
        GeneralCommand[] commands = GeneralCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (GeneralCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    private void processStudentCommand(String command, String[] commandParts) throws Exception {
        int neededCommandParts = 0;
        switch (command) {
            case "ENROLL":
                neededCommandParts = 5;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.enroll(commandParts);
                }
                break;
            case "ADVANCE":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.advance(commandParts);
                }
                break;
            case "CHANGE":
                neededCommandParts = 4;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.change(commandParts);
                }
                break;
            case "GRADUATE":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.graduate(commandParts);
                }
                break;
            case "INTERRUPT":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.interrupt(commandParts);
                }
                break;
            case "RESUME":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.resume(commandParts);
                }
                break;
            case "ENROLLIN":
                neededCommandParts = 3;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.enrollIn(commandParts);
                }
                break;
            case "ADDGRADE":
                neededCommandParts = 4;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentService.addGrade(commandParts);
                }
                break;
        }
    }

    private void processProtocolCommand(String command, String[] commandParts) {
        int neededCommandParts = 0;
        switch (command) {
            case "PRINT":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentReporter.print(commandParts);
                }
                break;
            case "PRINTALL":
                neededCommandParts = 3;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentReporter.printAll(commandParts);
                }
                break;
            case "PROTOCOL":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentReporter.protocol(commandParts);
                }
                break;
            case "REPORT":
                neededCommandParts = 2;
                if(checkCommandPartsLength(commandParts, neededCommandParts)) {
                    studentReporter.report(commandParts);
                }
                break;
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
}