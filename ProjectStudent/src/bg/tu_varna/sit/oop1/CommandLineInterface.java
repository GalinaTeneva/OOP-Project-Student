package bg.tu_varna.sit.oop1;

import java.io.IOException;

public interface CommandLineInterface {
    void open(String path) throws IOException;
    void close();
    void save(String path) throws IOException;
    void help(String path);
    void exit();
}
