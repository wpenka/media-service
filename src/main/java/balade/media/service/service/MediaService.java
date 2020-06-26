package balade.media.service.service;

import balade.media.common.StorageFileNotFoundException;
import balade.media.service.InconsistantSlideException;
import balade.media.service.domain.Processing;
import balade.media.service.domain.Slice;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class MediaService implements IMediaService {

    private final String uploadedFolder;
    private final Path rootLocation;
    private final Logger log = LoggerFactory.getLogger(MediaService.class);
    private IFile fileUtils;

    public MediaService(String uploadedFolder) {
        this.rootLocation = Paths.get(uploadedFolder);
        this.uploadedFolder = uploadedFolder;
        this.fileUtils = new DefaultFileUtil();
    }

    @Override
    public Processing appendData(Slice slice) {
        log.debug("appending Length [{}]  - file name {}", slice.getSize(), slice.getName());
        File file = getFile(slice.getName());
        try {
            if (isInvalidSlice(slice)) {
                throw new InconsistantSlideException(slice);
            }
            fileUtils.touch(file);
            if (file.exists() && !file.isDirectory()) {
                return getCompleteProcessing(slice, file);
            }
            file.createNewFile();
            fileUtils.writeByteArrayToFile(file, slice.getData(), true);
            if (file.length() >= slice.getSize()) {
                return getCompleteProcessing(slice, file);
            }
            return Processing.build(file.length());
        } catch (Exception e) {
            return getFailedProcessing(e);
        }
    }

    private boolean isInvalidSlice(Slice slice) {
        return slice.getSize() == 0 || slice.getName() == null || slice.getType() == null || "".equals(slice.getType());
    }

    private Processing getFailedProcessing(Exception e) {
        return Processing.build(0).setErrorMessage(e.getClass() + ": " + e.getMessage()).setComplete(true);
    }

    private Processing getCompleteProcessing(Slice slice, File file) {

        String[] filePathBlock = file.getAbsolutePath()
                .split(File.pathSeparator);

        StringBuilder expectSubPathName = new StringBuilder(filePathBlock[filePathBlock.length - 2]);
        expectSubPathName.append(File.pathSeparator)
                .append(filePathBlock[filePathBlock.length - 1]);

        return Processing.build(file.length())
                .setComplete(true)
                .setUrl(expectSubPathName.toString());

    }


    private File getFile(String name) {
        return this.fileUtils.createFile(uploadedFolder + name);
    }


    @Override
    public long getSize(String name) {
        File file = getFile(name);
        if (!file.exists())
            return 0;
        return file.length();
    }


    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public void setFileUtils(IFile fileUtils) {
        this.fileUtils = fileUtils;
    }
}
