package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.interfaces.CustomDeserializable;
import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;
import bg.tu_varna.sit.oop1.repositories.Repository;

import java.io.*;

public class FileManager<T> {
    private CustomSerializable<T> serializableService;
    private CustomDeserializable<T> deserializableService;
    private Repository<T> repository;

    public FileManager(CustomDeserializable<T> deserializableService, Repository<T> repository) {
        this(null, deserializableService, repository);
    }

    public FileManager(CustomSerializable<T> serializableService, CustomDeserializable<T> deserializableService, Repository<T> repository) {
        this.serializableService = serializableService;
        this.deserializableService = deserializableService;
        this.repository = repository;
    }

    public void open (String filePath) throws IOException {
        File file = new File(filePath);

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

    public void close() {
        repository.clear();
    }

    public void save(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T object : repository.getAll()) {
                String line = serializableService.serialize(object);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void saveAs(String filePath) throws IOException {
        save(filePath);
    }
}