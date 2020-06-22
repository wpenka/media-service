package balade.media.service.listener;

import balade.media.service.TestUtil;
import com.corundumstudio.socketio.Configuration;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootTest
@TestPropertySource(locations = "/test.application.properties")
class ConnexionListenerTest {

   final int  TIMEOUT=7000;
    static private Socket socket;
    private  final String ECHO_EVENT_NAME="echo";
    private  final String ECHO_BACK_EVENT_NAME="echoBack";

    @Autowired
    public Configuration rtcConfig;


    @BeforeEach
    void setUp() throws URISyntaxException {
        IO.Options opts = new IO.Options();
        opts.port=rtcConfig.getPort();
        socket = TestUtil.client(opts);
    }

    @Timeout(TIMEOUT)
    @DisplayName("Test connexion to server")
    @Test
    public void connectToLocalhost() throws URISyntaxException, InterruptedException {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        socket.on(Socket.EVENT_CONNECT, objects -> {
            socket.emit(ECHO_EVENT_NAME,"Eco");
            socket.on(ECHO_BACK_EVENT_NAME, args -> values.offer("done"));
        });
        socket.connect();
        values.take();
        socket.close();
    }

}