package balade.media.service.rest;

import balade.media.service.domain.Slice;
import balade.media.service.service.IMediaService;
import balade.media.service.service.MediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.application.properties")
class MediaControllerTest {

    @Value("${upload.tmp.folder}")
    String folder;

    @Autowired
    private MockMvc mockMvc;

    private  IMediaService mediaService;

    @Autowired
    private ObjectMapper objectMapper;

    File origin;
    File dest;
    @BeforeEach
    void setUp() {
        mediaService=  new MediaService(folder);
        createOriginFile();
        dest = new File(folder+"dest-data.txt");
        dest.deleteOnExit();
    }


    @Test
    void appendSlice() throws Exception {
        byte[] fileContent = Files.readAllBytes(origin.toPath());
        Slice slice= new Slice(dest.getName(),"text",origin.length(),fileContent);
        mockMvc.perform(post("/api/slice",slice)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(slice)))
                .andExpect(status().isOk());
        assertEquals(origin.length(),mediaService.getSize(slice.getName()));
    }

    private void createOriginFile(){
         origin = new File(folder+"origin-data.txt");
        try {
            if(!origin.exists())
                origin.createNewFile();
            String fileData = "If you want to create a new file and at the same time";
            FileOutputStream fos = new FileOutputStream(folder+"origin-data.txt");
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        origin.deleteOnExit();
        dest.deleteOnExit();
    }
}