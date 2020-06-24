package balade.media.service.service;

import java.io.File;
import java.io.IOException;

public interface IFile {

    void touch(File file) throws IOException;

    File createFile(String fileName);

    void writeByteArrayToFile(File file , byte[] data , boolean append) throws IOException;
}
