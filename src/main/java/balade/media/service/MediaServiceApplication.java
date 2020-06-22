package balade.media.service;


import balade.media.service.service.IMediaService;
import balade.media.service.service.MediaService;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Objects;
import java.util.Set;

@SpringBootApplication
public class MediaServiceApplication {
    private final Logger log = LoggerFactory.getLogger(MediaServiceApplication.class);

    @Autowired
	private Environment env;

	@Bean
	public SocketIOServer socketIOServer() {

		return new SocketIOServer(rtcConfig ());
	}

	@Bean
	public IMediaService mediaService(){
	    String uploadFolder=Objects.requireNonNull(env.getProperty("upload.tmp.folder"));
        try {
            Set<PosixFilePermission> permissions= PosixFilePermissions.fromString("rwxrwxrwx");
            Path dataDir = Paths.get(uploadFolder);
            if(!Files.exists(dataDir))
                Files.createDirectories(dataDir);
        }
        catch (IOException e) {
            log.error("{}", e.getMessage());
        }
		return new MediaService(uploadFolder);
		}

	public static void main(String[] args) {
		SpringApplication.run(MediaServiceApplication.class, args);
	}

	@Bean
	public Configuration rtcConfig (){
		Configuration config = new Configuration();
		config.setHostname(env.getProperty("rt-server.host"));
		config.setPort(Integer.parseInt(env.getProperty("rt-server.port")));
		return config;
	}
	/*@RequestMapping(path = "/test-swagger", method= RequestMethod.GET)
	public String home() {
		return "Spring is here!";
	}*/
}
