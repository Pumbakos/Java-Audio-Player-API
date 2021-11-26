package pl.pumbakos.japwebservice.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MockMvc;
import pl.pumbakos.japwebservice.albummodule.controllers.AlbumController;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.generators.AlbumGenerator;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static pl.pumbakos.japwebservice.japresources.DateFormat.ISO;

@WebMvcTest(controllers = {AlbumController.class})
public class AlbumControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new GsonBuilder().setDateFormat(ISO).create();

    @MockBean
    private AlbumController controller;

    @Autowired
    private MockMvc mock;

    @Test
    @SneakyThrows
    @DisplayName("GET empty albums list")
    public void getEmptyAlbumsList() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.noContent().build());

        int status = mock.perform(get("/albums/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET all albums")
    public void getAllAlbums() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(get("/albums/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET not existing author")
    public void getNotExistingAlbum() {
        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(get("/albums/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET existing author")
    public void getExistingAlbum() {
        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(get("/albums/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST new author")
    public void postNewAlbum() {
        Album completeAlbum = AlbumGenerator.createCompleteAlbum();
        String json = gson.toJson(completeAlbum);

        Mockito.when(controller.save(Mockito.any(Album.class))).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(post("/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST empty author")
    public void postEmptyAlbum() {
        Album emptyAlbum = AlbumGenerator.createEmptyAlbum();
        String json = gson.toJson(emptyAlbum);

        Mockito.when(controller.save(Mockito.any(Album.class))).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(post("/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST blank author")
    public void postBlankAlbum() {
        Album blankAlbum = AlbumGenerator.createBlankAlbum();
        String json = gson.toJson(blankAlbum);

        Mockito.when(controller.save(Mockito.any(Album.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        int status = mock.perform(post("/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with correct data")
    public void updateAlbumWithCorrectData() {
        Album completeAlbum = AlbumGenerator.createCompleteAlbum();
        String json = gson.toJson(completeAlbum);

        Mockito.when(controller.update(Mockito.any(Album.class), Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with blank data")
    public void updateAlbumWithBlankData() {
        Album blankAlbum = AlbumGenerator.createBlankAlbum();
        String json = gson.toJson(blankAlbum);

        Mockito.when(controller.update(Mockito.any(Album.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with empty data")
    public void updateAlbumWithEmptyData() {
        Album emptyAlbum = AlbumGenerator.createEmptyAlbum();
        String json = gson.toJson(emptyAlbum);

        Mockito.when(controller.update(Mockito.any(Album.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update not existing author with incorrect data")
    public void updateNotExistingAlbumWithIncorrectData() {
        Album blankAlbum = AlbumGenerator.createBlankAlbum();
        String json = gson.toJson(blankAlbum);

        Mockito.when(controller.update(Mockito.any(Album.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update not existing author with correct data")
    public void updateNotExistingAlbumWithCorrectData() {
        Album completeAlbum = AlbumGenerator.createCompleteAlbum();
        String json = gson.toJson(completeAlbum);

        Mockito.when(controller.update(Mockito.any(Album.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE existing author")
    public void deleteAlbum() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(delete("/albums/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE not existing author")
    public void deleteNotExistingAlbum() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(delete("/albums/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }
}
