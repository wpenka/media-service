package balade.media.service;

import balade.media.service.domain.Slice;
import balade.media.service.listener.SliceListener;
import balade.media.service.service.IMediaService;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class WebRtcConfig {

    private static final Logger log = LoggerFactory.getLogger(WebRtcConfig.class);
    private final IMediaService mediaService;
    private final SocketIOServer server;
    @Autowired
    public WebRtcConfig(SocketIOServer server, IMediaService mediaService) {
        this.mediaService = mediaService;
        this.server=server;
        this.server.addConnectListener(onConnected());
        this.server.addDisconnectListener(onDisconnected());
        this.server.addEventListener("file", Slice.class, onSlicetReceived());
        this.server.addEventListener("echo",String.class, echoListener());
    }

    private DataListener<Slice> onSlicetReceived() {
        return new SliceListener(mediaService);
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
            client.sendEvent("connected",new Date());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.debug("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
        };
    }
    @Bean
    private DataListener<String>  echoListener(){
        return (client, data, ackSender) -> {
            log.debug("Client[{}] - echo from client module.  {}", client.getSessionId().toString(),data);
            client.sendEvent("echoBack", data);
        };
    }
}
