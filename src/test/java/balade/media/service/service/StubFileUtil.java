package balade.media.service.service;

import java.io.File;
import java.io.IOException;

public class StubFileUtil implements IFile {
    @Override
    public void touch(File file) throws IOException {

    }

    @Override
    public File createFile(String fileName) {
        return null;
    }

    @Override
    public void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {

    }
}
