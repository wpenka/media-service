package balade.media.service.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DefaultFileUtil implements IFile{
    @Override
    public void touch(File file) throws IOException {
        FileUtils.touch(file);
    }

    @Override
    public File createFile(String fileName) {
        return new File(fileName);
    }

    @Override
    public void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        FileUtils.writeByteArrayToFile(file, data,true);
    }
}
