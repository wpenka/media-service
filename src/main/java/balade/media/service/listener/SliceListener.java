package balade.media.service.listener;

import balade.media.service.WebRtcConfig;
import balade.media.service.domain.Processing;
import balade.media.service.domain.Slice;
import balade.media.service.service.IMediaService;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SliceListener implements DataListener<Slice> {
    private static final Logger log = LoggerFactory.getLogger(WebRtcConfig.class);
    private final IMediaService mediaService;
    private  final String ERROR_EVENT_NAME ="sliceError";

    public SliceListener( IMediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public void onData(SocketIOClient client, Slice data, AckRequest ackSender) throws Exception {
        log.debug("Client[{}] - Received Slice {} - file name {}, data [{}]", client.getSessionId().toString(), data.getType(),data.getName(),data);
        Processing processing=mediaService.appendData(data);
        client.sendEvent(data.getName()!=null&&!data.getName().isEmpty()?data.getName(): ERROR_EVENT_NAME, processing);
    }


}
