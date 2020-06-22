package balade.media.service;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.logging.Logger;

public abstract class TestUtil {

    private static final Logger logger = Logger.getLogger(TestUtil.class.getName());

    static  Socket client() throws URISyntaxException {
        return client(createOptions());
    }

    static Socket client(String path) throws URISyntaxException {
        return client(path, createOptions());
    }

    public static Socket client(IO.Options opts) throws URISyntaxException {
        return client(nsp(), opts);
    }

    static Socket client(String path, IO.Options opts) throws URISyntaxException {
        return IO.socket(uri(opts.port) + path, opts);
    }

    static String uri(int port) {
        return "http://localhost:"+port;
    }

    static String nsp() {
        return "/";
    }

    static IO.Options createOptions() {
        IO.Options opts = new IO.Options();
        opts.port=9092;
        opts.forceNew = true;
        return opts;
    }

}
