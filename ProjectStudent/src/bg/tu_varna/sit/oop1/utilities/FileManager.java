package bg.tu_varna.sit.oop1.utilities;

import bg.tu_varna.sit.oop1.enums.UserMessages;
import bg.tu_varna.sit.oop1.serialization.deserializer.CustomDeserializable;
import bg.tu_varna.sit.oop1.serialization.serializer.CustomSerializable;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.io.*;

/**
 * The FileManager class provides functionalities to manage file operations.
 * It uses custom serialization and deserialization services.
 *
 * @param <T> The type of objects that this file manager will handle.
 */
public class FileManager<T> {
    private CustomSerializable<T> serializableService;
    private CustomDeserializable<T> deserializableService;
    private Repository<T> repository;

    /**
     * Constructs a FileManager with the specified deserializable service and repository.
     *
     * @param deserializableService The service for deserializing objects.
     * @param repository The repository to store and manage objects.
     */
    public FileManager(CustomDeserializable<T> deserializableService, Repository<T> repository) {
        this(null, deserializableService, repository);
    }

    /**
     * Constructs a FileManager with specified serializable, deserializable services, and repository.
     *
     * @param serializableService The service for serializing objects.
     * @param deserializableService The service for deserializing objects.
     * @param repository The repository to store and manage objects.
     */
    public FileManager(CustomSerializable<T> serializableService, CustomDeserializable<T> deserializableService, Repository<T> repository) {
        this.serializableService = serializableService;
        this.deserializableService = deserializableService;
        this.repository = repository;
    }

    /**
     * Opens a file from the given file path and reads it.
     * Deserializes the content of the file and store the objects in the repository.
     *
     * @param filePath The path of the file to open.
     * @throws IOException If an error occurs while reading the file.
     */
    public void open (String filePath) throws IOException {
        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();

        boolean isDirExists = validateDirectory(absolutePath);
        if(!isDirExists){
            throw new IOException(UserMessages.MISSING_DIRECTORY_ERROR.message);
        }


        if (!file.exists()) {
            file.createNewFile();
            return; // New file created, nothing to load.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T object = deserializableService.deserialize(line);
                repository.addNew(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the repository.
     */
    public void close() {
        repository.clear();
    }

    /**
     * Saves the objects from the repository to a file at the specified file path.
     * The objects are serialized before being written to the file.
     *
     * @param filePath The path of the file where the objects will be saved.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void save(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T object : repository.getAll()) {
                String line = serializableService.serialize(object);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Validates if the specified path is a directory.
     * This method takes a file path as a string and checks if it leads to a directory.
     *
     * @param path The file path as a String.
     * @return true if the path is a directory and false otherwise.
     */
    private boolean validateDirectory(String path) {
        StringBuilder sb = new StringBuilder();
        String[] filePathParts = path.split("\\\\");

        for (int i = 0; i < filePathParts.length - 2; i++) {
            sb.append(filePathParts[i]).append("\\");
        }

        File directory = new File(sb.toString());

        // Check if the directory already exists
        if (directory.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }
}