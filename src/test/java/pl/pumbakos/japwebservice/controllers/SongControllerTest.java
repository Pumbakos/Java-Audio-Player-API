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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import pl.pumbakos.japwebservice.generators.SongGenerator;
import pl.pumbakos.japwebservice.songmodule.controllers.SongController;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Song completeSong = SongGenerator.createCompleteSong();
        Song anotherCompleteSong = SongGenerator.createAnotherCompleteSong();

        List<Song> songs = List.of(completeSong, anotherCompleteSong);

        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.ok(songs));

        String contentAsString = mock.perform(get("/songs/info/all"))
                .andReturn().getResponse().getContentAsString();

        List<Song> result = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        Assertions.assertEquals(completeSong.getId(), result.get(0).getId());
        Assertions.assertEquals(anotherCompleteSong.getId(), result.get(1).getId());
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

//    @Test
//    @DisplayName("GET all titles")
//    @SneakyThrows
//    public void getAllTitles(){
//        Mockito.when(controller.getTitles()).thenReturn(ResponseEntity.ok(Mockito.any(String.class)));
//        int status = mock.perform(get("/songs/all/"))
//                .andReturn().getResponse().getStatus();
//
//        Assertions.assertEquals(HttpStatus.OK.value(), status);
//    }
//
//    @Test
//    @DisplayName("GET all titles - empty")
//    @SneakyThrows
//    public void getAllTitlesEmpty(){
//        Mockito.when(controller.getTitles()).thenReturn(ResponseEntity.notFound().build());
//        int status = mock.perform(get("/songs/all/"))
//                .andReturn().getResponse().getStatus();
//
//        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
//    }
}
