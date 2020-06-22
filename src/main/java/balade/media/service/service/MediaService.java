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
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MediaService implements IMediaService {

    private final String uploadedFolder;
    private final Path rootLocation;
    private final Logger log = LoggerFactory.getLogger(MediaService.class);

    public MediaService(String uploadedFolder) {
        this.rootLocation = Paths.get(uploadedFolder);
        this.uploadedFolder = uploadedFolder;
    }

    @Override
    public Processing appendData(Slice slice) {
        log.debug("appending Length [{}]  - file name {}", slice.getSize(),slice.getName());
        File file = new File(uploadedFolder+slice.getName());
        try {
            if (slice.getSize()==0||"".equals(slice.getName()==null||"".equals(slice.getType())||slice.getType()==null))
            throw new InconsistantSlideException(slice);
            FileUtils.touch(file);
        if(!file.exists()||file.isDirectory()){
            file.createNewFile();
        }
        FileUtils.writeByteArrayToFile(file, slice.getData(),true);
        if (file.length() >= slice.getSize()) {
            return Processing.build(file.length())
                    .setComplete(true)
                    .setUrl(slice.getName());
        }
        return Processing.build(file.length());
        } catch (Exception e) {
            return Processing.build(0).setErrorMessage(e.getClass()+": "+e.getMessage()).setComplete(true);
        }
    }


    @Override
    public long getSize(String name) {
        File file = new File(uploadedFolder+name);
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
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}