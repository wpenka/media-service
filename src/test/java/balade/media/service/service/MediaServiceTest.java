package balade.media.service.service;

import balade.media.service.InconsistantSlideException;
import balade.media.service.domain.Processing;
import balade.media.service.domain.Slice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaServiceTest {

    private File file ;

    @BeforeEach
    void setUp() {

        file = mock(File.class);
    }

    @Test
    void shouldReturnProcessingWithErrorMessageSet() {

        Slice slice = new Slice();
        slice.setSize(0);
        slice.setName(null);
        MediaService mediaService = new MediaService("filelocation");

        assertThat(mediaService.appendData(slice).getErrorMessage().isEmpty()).isFalse();
    }

    @Test
    void shouldCallTouchFile() throws IOException {
        Slice slice = new Slice("QWERTY" ,"A",2 ,"ertyy".getBytes());
        MediaService mediaService = new MediaService("filelocation");
        IFile fileutils = mock(DefaultFileUtil.class);
        mediaService.setFileUtils(fileutils);
        mediaService.appendData(slice);
        verify(fileutils , times(1)).touch(any());
    }

    @Test
    void shouldCreateNewFile() {
        Slice slice = new Slice("QWERTY" ,"A",2 ,"ertyy".getBytes());
        MediaService mediaService = new MediaService("filelocation");
        IFile fileutils = mock(DefaultFileUtil.class);
        when(fileutils.createFile(anyString())).thenReturn(file);
        when(file.exists()).thenReturn(false);
        when(file.isDirectory()).thenReturn(true);
        mediaService.setFileUtils(fileutils);
        mediaService.appendData(slice);
        verify(fileutils , times(1)).createFile("filelocation"+slice.getName());
    }

    @Test
    void shouldCallwriteByteArrayToFile() throws IOException {
        Slice slice = new Slice("QWERTY" ,"A",2 ,"ertyy".getBytes());
        MediaService mediaService = new MediaService("filelocation");
        IFile fileutils = mock(DefaultFileUtil.class);
        mediaService.setFileUtils(fileutils);
        when(fileutils.createFile(anyString())).thenReturn(file);
        when(file.exists()).thenReturn(false);
        when(file.isDirectory()).thenReturn(true);
        mediaService.appendData(slice);
        verify(fileutils , times(1)).writeByteArrayToFile(file , slice.getData() , true);
    }

    @Test
    void shouldReturnCompleteProcessingWithFileLength() {
        Slice slice = new Slice("QWERTY" ,"A",2 ,"ertyy".getBytes());
        MediaService mediaService = new MediaService("filelocation");
        IFile fileutils = mock(DefaultFileUtil.class);
        mediaService.setFileUtils(fileutils);
        when(fileutils.createFile(anyString())).thenReturn(file);
        when(file.exists()).thenReturn(false);
        when(file.isDirectory()).thenReturn(true);
        when(file.length()).thenReturn(2L);
        Processing processing =mediaService.appendData(slice);
        assertThat(processing.getCurrentSize()).isEqualTo(file.length());
        assertThat(processing.getUrl()).isEqualTo(slice.getName());
    }
}