package balade.media.service.listener;
import balade.media.service.TestUtil;
import balade.media.service.service.IMediaService;
import balade.media.service.service.MediaService;
import com.corundumstudio.socketio.Configuration;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.application.properties")
class SliceListenerTest {
    @Value("${upload.tmp.folder}")
    String folder;
    final int  TIMEOUT=7000;
    private IMediaService mediaService;
    private File origin;
    private  final String ERROR_EVENT_NAME ="sliceError";
    static private Socket socket;

    @Autowired
    public Configuration rtcConfig;


    @BeforeEach
    void setUp() throws URISyntaxException {
        IO.Options opts = new IO.Options();
        opts.port=rtcConfig.getPort();
        socket = TestUtil.client(opts);
        origin=createOriginFile();
        mediaService=  new MediaService(folder);
    }
    @Timeout(TIMEOUT)
    @DisplayName("senTSliceWithError")
    @Test
    public  void senTSliceWithError() throws InterruptedException, IOException, JSONException {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<>();
        JSONObject signal = new JSONObject();
        signal.put("name", JSONObject.NULL);
        signal.put("type", JSONObject.NULL);
        signal.put("size", 0);
        signal.put("data", Files.readAllBytes(origin.toPath()));
        socket.on(Socket.EVENT_CONNECT, objects -> {
            socket.emit("file", signal);
            socket.on(ERROR_EVENT_NAME, args -> {
                values.offer(args[0]);
            });

        });
        socket.connect();
        JSONObject obj = (JSONObject)values.take();
        assertEquals(obj.get("complete"), true);
        assertEquals(obj.get("currentSize"), 0);
        assertNotNull(obj.get("errorMessage"));
    }


    @Timeout(TIMEOUT)
    @DisplayName("senTSlideLastSlice")
    @Test
    public  void senTLastSlice() throws InterruptedException, IOException, JSONException {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<>();
        JSONObject signal = new JSONObject();
        signal.put("name", origin.getName());
        signal.put("type", MediaType.IMAGE_PNG_VALUE);
        signal.put("size", origin.length());
        signal.put("data", Files.readAllBytes(origin.toPath()));
        socket.on(Socket.EVENT_CONNECT, objects -> {
            socket.emit("file", signal);
            socket.on(origin.getName(), args -> {
                values.offer(args[0]);
            });
        });
        socket.connect();
        JSONObject obj = (JSONObject)values.take();
        assertEquals(obj.get("complete"), true);
        assertNotNull(obj.get("url"));
    }


    @AfterEach
    void tearDown() {
        socket.disconnect();
        origin.deleteOnExit();
    }

    private File createOriginFile(){
        File origin = new File(folder+ UUID.randomUUID().toString());
        try {
            if(!origin.exists())
                origin.createNewFile();
            String fileData = "If you want to create a new file and at the same time";
            System.out.println(fileData);
            FileOutputStream fos = new FileOutputStream(folder+origin.getName());
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();
            return origin;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return origin;
    }
}