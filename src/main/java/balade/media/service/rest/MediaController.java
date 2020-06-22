package balade.media.service.rest;

import balade.media.common.BadRequestAlertException;
import balade.media.common.HeaderUtil;
import balade.media.service.domain.Processing;
import balade.media.service.domain.Slice;
import balade.media.service.service.IMediaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path="/api")
@Api(value = "File slice Receiver", description = "Manage file's part comming from callers ")
public class MediaController {
    private final Logger log = LoggerFactory.getLogger(MediaController.class);
    private final IMediaService mediaService;
    private String applicationName;

    public MediaController(IMediaService mediaService, Environment env) {
        this.mediaService = mediaService;
        applicationName= env.getProperty("app.name");
    }

    @PostMapping(path="/slice")
    @ApiOperation(value = "Post bytes in slice object")
    public ResponseEntity<Processing> appendSlice(@RequestBody Slice slice) throws BadRequestAlertException, IOException {
        log.debug("REST request to append slice : {}", slice.getData());
        Processing result = mediaService.appendData(slice);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, slice.getName(), slice.toString()))
                .body(result);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @ApiOperation(value = "Serve file bu file name")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource file = mediaService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_LOCATION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
