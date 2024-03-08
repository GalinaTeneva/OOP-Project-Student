package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.GeneralCommand;
import bg.tu_varna.sit.oop1.enums.ReportCommand;
import bg.tu_varna.sit.oop1.enums.StudentCommand;
import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.reporters.StudentReporter;
import bg.tu_varna.sit.oop1.repositories.ProgramRepository;
import bg.tu_varna.sit.oop1.repositories.StudentRepository;
import bg.tu_varna.sit.oop1.services.StudentService;

import java.util.HashSet;
import java.util.Scanner;

/**
 * The Execute class is handling user inputs and executing corresponding commands.
 */
public class Execute {
    private String pathToFileHelp = ".\\HelpInfo.txt";

    private Scanner scanner;
    private StudentService studentService;

    private StudentReporter studentReporter;
    private StudentRepository studentRepository;
    private ProgramRepository programRepository;

    private CommandLine commandLine;

    /**
     * Constructor to initialize the repositories, serializers, deserializers, services, and file managers.
     */
    public Execute() {
        this.studentRepository = new StudentRepository();
        this.programRepository = new ProgramRepository();

        this.studentService = new StudentService(studentRepository, programRepository);

        this.studentReporter = new StudentReporter(studentRepository);
        this.scanner = new Scanner(System.in);

        this.commandLine = new CommandLine(studentRepository, programRepository);
    }

    /**
     * Starts the execution of the application.
     * It runs in a loop, processing input commands until an exit command is given.
     */
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
                this.commandLine.exit();
            }

            if (command.equals("HELP")) {
                this.commandLine.help(pathToFileHelp);
                continue;
            }

            try {
                if(command.equals("OPEN") && !isFileLoaded) {
                    necessaryCommandParts = 2;
                    if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                        filePath = commandParts[1];
                        fileName = getFileName(filePath);

                        this.commandLine.open(filePath);
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
                                this.commandLine.close();
                                isFileLoaded = false;
                                System.out.println("Successfully closed " + fileName);
                            }
                            break;
                        case "SAVE":
                            necessaryCommandParts = 1;
                            if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                                this.commandLine.save(filePath);
                                System.out.println("Successfully saved " + fileName);
                            }
                            break;
                        case "SAVEAS":
                            necessaryCommandParts = 2;
                            if(checkCommandPartsLength(commandParts, necessaryCommandParts)) {
                                String newPath = commandParts[1];
                                String anotherFileName = getFileName(newPath);
                                this.commandLine.save(newPath);
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

    /**
     * Fetches all student-related commands from the StudentCommand enum.
     *
     * @return A HashSet containing student commands as strings.
     */
    private HashSet<String> getStudentCommands() {
        StudentCommand[] commands = StudentCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (StudentCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    /**
     * Fetches all report-related commands from the ReportCommand enum.
     *
     * @return A HashSet containing report commands as strings.
     */
    private HashSet<String> getReportCommands() {
        ReportCommand[] commands = ReportCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (ReportCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    /**
     * Fetches all general commands from the GeneralCommand enum.
     *
     * @return A HashSet containing general commands as strings.
     */
    private HashSet<String> getGeneralCommands() {
        GeneralCommand[] commands = GeneralCommand.values();
        HashSet<String> commandSet = new HashSet<>();

        for (GeneralCommand command : commands) {
            commandSet.add(command.toString());
        }

        return commandSet;
    }

    /**
     * Processes student commands by calling the corresponding method in StudentService.
     *
     * @param command The command to be processed.
     * @param commandParts The arguments of the command as array of strings.
     * @throws Exception If there is an issue in processing the command.
     */
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

    /**
     * Processes student commands by calling the corresponding method in StudentReporter.
     *
     * @param command The command to be processed.
     * @param commandParts The arguments of the command as array of strings.
     */
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

    /**
     * Validates the length of the command parts array considering the expected count.
     *
     * @param parts The command parts array.
     * @param count The expected count of parts.
     * @return true if the length matches the expected count.
     * @throws IllegalArgumentException If the length of the parts array does not match the expected count.
     */
    private boolean checkCommandPartsLength(String[] parts, int count) {
        if (parts.length != count) {
            throw new IllegalArgumentException(UserMessages.WRONG_ARGUMENTS_COUNT.message);
        }

        return true;
    }

    /**
     * Extracts the file name from a given file path.
     *
     * @param path The file path.
     * @return The extracted file name.
     */
    private String getFileName(String path) {
        String[] filePathParts = path.split("\\\\");
        String fileName = filePathParts[filePathParts.length - 1];
        return  fileName;
    }
}