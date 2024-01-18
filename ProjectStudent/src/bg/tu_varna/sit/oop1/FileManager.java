package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.exceptions.DeserializationException;
import bg.tu_varna.sit.oop1.exceptions.ProgramException;
import bg.tu_varna.sit.oop1.exceptions.StudentException;
import bg.tu_varna.sit.oop1.exceptions.SubjectException;
import bg.tu_varna.sit.oop1.interfaces.CustomDeserializable;
import bg.tu_varna.sit.oop1.interfaces.CustomSerializable;

import java.io.*;
import java.util.*;

public class FileManager<T> {
    private String currentFilePath;
    private CustomSerializable<T> serializableService;
    private CustomDeserializable<T> deserializableService;
    private Collection<T> objectCollection;

    public FileManager(CustomDeserializable<T> deserializableService, Collection<T> objectCollection) {
        this(null, deserializableService, objectCollection);
    }

    public FileManager(CustomSerializable<T> serializableService, CustomDeserializable<T> deserializableService, Collection<T> objectCollection) {
        this.serializableService = serializableService;
        this.deserializableService = deserializableService;
        this.objectCollection = objectCollection;
    }

    public void open (String filePath) throws IOException, DeserializationException {
        this.currentFilePath = filePath;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
            return; // New file created, nothing to load.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T object = deserializableService.deserialize(line);
                objectCollection.add(object);
            }
        } catch (StudentException e) {
            e.printStackTrace();
        } catch (SubjectException e) {
            e.printStackTrace();
        } catch (ProgramException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.currentFilePath = null;
        objectCollection.clear();
    }

    public void save() throws IOException {
        if (currentFilePath == null) {
            throw new IllegalStateException("No file is currently open");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFilePath))) {
            for (T object : objectCollection) {
                String line = serializableService.serialize(object);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public void saveAs(String filePath) throws IOException {
        this.currentFilePath = filePath;
        save();
    }
}
