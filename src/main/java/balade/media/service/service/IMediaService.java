package balade.media.service.service;

import balade.media.service.domain.Slice;
import balade.media.service.domain.Processing;
import org.springframework.core.io.Resource;
import java.io.IOException;

public interface IMediaService {

    Processing appendData(Slice data) throws IOException;

    long getSize(String file);

    Resource loadAsResource(String filename);
}
