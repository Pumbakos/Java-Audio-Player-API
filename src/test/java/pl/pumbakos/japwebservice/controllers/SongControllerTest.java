package pl.pumbakos.japwebservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.pumbakos.japwebservice.generators.SongGenerator;
import pl.pumbakos.japwebservice.songmodule.controllers.SongController;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static pl.pumbakos.japwebservice.japresources.DateFormat.ISO;

@WebMvcTest(controllers = {SongController.class})
public class SongControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new GsonBuilder().setDateFormat(ISO).create();

    @MockBean
    public SongController controller;

    @Autowired
    private MockMvc mock;

    @Test
    @DisplayName("GET - download existing song")
    @SneakyThrows
    public void downloadExistingSong(){
        Mockito.when(controller.download(Mockito.anyString())).thenReturn(ResponseEntity.ok().build());
        int status = mock.perform(get("/songs?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("GET - download non existing song")
    @SneakyThrows
    public void downloadNonExistingSong(){
        Mockito.when(controller.download(Mockito.anyString())).thenReturn(ResponseEntity.notFound().build());
        int status = mock.perform(get("/songs?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @DisplayName("POST - update songs with supported extension")
    @SneakyThrows
    public void uploadSongWithSupportedExtension(){
        Mockito.when(controller.upload(Mockito.anyList())).thenReturn(ResponseEntity.ok().build());

        MockMultipartFile firstFile = new MockMultipartFile("files", "test1.wav", MediaType.MULTIPART_FORM_DATA_VALUE, "test1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "test2.wav", MediaType.MULTIPART_FORM_DATA_VALUE, "test2".getBytes());

        int status = mock.perform(multipart("/songs/")
                        .file(firstFile)
                        .file(secondFile))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("POST - upload songs with unsupported extension")
    @SneakyThrows
    public void uploadSongWithUnsupportedExtension() {
        Mockito.when(controller.upload(Mockito.anyList())).thenReturn(ResponseEntity.unprocessableEntity().build());

        MockMultipartFile firstFile = new MockMultipartFile("files", "test1.ogg", MediaType.MULTIPART_FORM_DATA_VALUE, "test1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "test2.ogg", MediaType.MULTIPART_FORM_DATA_VALUE, "test2".getBytes());

        int status = mock.perform(multipart("/songs/")
                        .file(firstFile)
                        .file(secondFile))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
    }

    @Test
    @DisplayName("POST - upload songs with no files")
    @SneakyThrows
    public void uploadSongWithNoFiles() {
        Mockito.when(controller.upload(Mockito.anyList())).thenReturn(ResponseEntity.unprocessableEntity().build());

        int status = mock.perform(multipart("/songs/")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @DisplayName("POST - upload songs with supported extension and force IOException")
    @SneakyThrows
    public void uploadSongWithSupportedExtensionForceIOException() {
        Mockito.when(controller.upload(Mockito.anyList())).thenReturn(ResponseEntity.internalServerError().build());

        MockMultipartFile firstFile = new MockMultipartFile("files", "test1.ogg", MediaType.MULTIPART_FORM_DATA_VALUE, "test1".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("files", "test2.ogg", MediaType.MULTIPART_FORM_DATA_VALUE, "test2".getBytes());

        int status = mock.perform(multipart("/songs/")
                        .file(firstFile)
                        .file(secondFile))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
    }

    @Test
    @DisplayName("GET empty list of songs")
    @SneakyThrows
    public void getEmptySongList() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(get("/songs/info/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @DisplayName("Get all songs")
    @SneakyThrows
    public void getAllSongs() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(get("/songs/info/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("GET existing song by title")
    @SneakyThrows
    public void getExistingSongByTitle(){
        Mockito.when(controller.get(Mockito.anyString())).thenReturn(ResponseEntity.ok(Mockito.any(Song.class)));
        int status = mock.perform(get("/songs/info?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("GET not existing song by title")
    @SneakyThrows
    public void getNotExistingSongByTitle(){
        Mockito.when(controller.get(Mockito.anyString())).thenReturn(ResponseEntity.notFound().build());
        int status = mock.perform(get("/songs/info?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @DisplayName("GET all titles")
    @SneakyThrows
    public void getAllTitles(){
        Mockito.when(controller.getTitles()).thenReturn(ResponseEntity.ok().build());
        int status = mock.perform(get("/songs/all/"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("GET all titles - empty")
    @SneakyThrows
    public void getAllTitlesEmpty(){
        Mockito.when(controller.getTitles()).thenReturn(ResponseEntity.notFound().build());
        int status = mock.perform(get("/songs/all/"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @DisplayName("GET file size for existing song")
    @SneakyThrows
    public void getFileSizeForExistingSong(){
        Mockito.when(controller.getFileSize(Mockito.anyString())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(get("/songs/size?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("GET file size for not existing song")
    @SneakyThrows
    public void getFileSizeNotForExistingSong(){
        Mockito.when(controller.getFileSize(Mockito.anyString())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(get("/songs/size?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @DisplayName("PUT - update existing song with valid data")
    @SneakyThrows
    public void updateExistingSongWithValidData(){
        Song completeSong = SongGenerator.createCompleteSong();
        String json = gson.toJson(completeSong);

        Mockito.when(controller.update(Mockito.any(Song.class), Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(put("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("PUT - update not existing song with valid data")
    @SneakyThrows
    public void updateNotExistingSongWithValidData(){
        Song completeSong = SongGenerator.createCompleteSong();
        String json = gson.toJson(completeSong);

        Mockito.when(controller.update(Mockito.any(Song.class), Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(put("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }


    @Test
    @DisplayName("PUT - update existing song with invalid data")
    @SneakyThrows
    public void updateExistingSongWithInvalidData(){
        Song completeSong = SongGenerator.createCompleteSong();
        String json = gson.toJson(completeSong);

        Mockito.when(controller.update(Mockito.any(Song.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @DisplayName("PUT - update not existing song with invalid data")
    @SneakyThrows
    public void updateNotExistingSongWithInvalidData(){
        Song emptySong = SongGenerator.createEmptySong();
        String json = gson.toJson(emptySong);

        Mockito.when(controller.update(Mockito.any(Song.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/songs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @DisplayName("DELETE - delete existing song")
    @SneakyThrows
    public void deleteExistingSong(){
        Mockito.when(controller.delete(Mockito.anyString())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(delete("/songs?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @DisplayName("DELETE - delete not existing song")
    @SneakyThrows
    public void deleteNotExistingSong(){
        Mockito.when(controller.delete(Mockito.anyString())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(delete("/songs?filename=title"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }
}
