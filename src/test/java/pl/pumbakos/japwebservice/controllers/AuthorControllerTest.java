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
import pl.pumbakos.japwebservice.authormodule.controllers.AuthorController;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.generators.AuthorGenerator;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static pl.pumbakos.japwebservice.japresources.DateFormat.ISO;

@WebMvcTest(controllers = {AuthorController.class})
public class AuthorControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new GsonBuilder().setDateFormat(ISO).create();

    @MockBean
    private AuthorController controller;

    @Autowired
    private MockMvc mock;

    @Test
    @SneakyThrows
    @DisplayName("GET empty authors list")
    public void getEmptyAuthorsList() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.noContent().build());

        int status = mock.perform(get("/authors/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET all authors")
    public void getAllAuthors() {
        Author completeAuthor = AuthorGenerator.createCompleteAuthor();
        Author anotherCompleteAuthor = AuthorGenerator.createAnotherCompleteAuthor();

        List<Author> authors = Arrays.asList(completeAuthor, anotherCompleteAuthor);

        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.ok(authors));

        String contentAsString = mock.perform(get("/authors/all"))
                .andReturn().getResponse().getContentAsString();

        List<Author> result = mapper.readValue(contentAsString, new TypeReference<>() {});

        Assertions.assertEquals(completeAuthor.getId(), result.get(0).getId());
        Assertions.assertEquals(anotherCompleteAuthor.getId(), result.get(1).getId());
    }

    @Test
    @SneakyThrows
    @DisplayName("GET not existing author")
    public void getNotExistingAuthor() {
        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(get("/authors/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("GET existing author")
    public void getExistingAuthor() {
        Author completeAuthor = AuthorGenerator.createCompleteAuthor();

        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.ok(completeAuthor));

        int status = mock.perform(get("/authors/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST new author")
    public void postNewAuthor() {
        Author completeAuthor = AuthorGenerator.createCompleteAuthor();
        String json = gson.toJson(completeAuthor);

        Mockito.when(controller.save(Mockito.any(Author.class))).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST empty author")
    public void postEmptyAuthor() {
        Author emptyAuthor = AuthorGenerator.createEmptyAuthor();
        String json = gson.toJson(emptyAuthor);

        Mockito.when(controller.save(Mockito.any(Author.class))).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("POST blank author")
    public void postBlankAuthor() {
        Author blankAuthor = AuthorGenerator.createBlankAuthor();
        String json = gson.toJson(blankAuthor);

        Mockito.when(controller.save(Mockito.any(Author.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        int status = mock.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with correct data")
    public void updateAuthorWithCorrectData() {
        Author completeAuthor = AuthorGenerator.createCompleteAuthor();
        String json = gson.toJson(completeAuthor);

        Mockito.when(controller.update(Mockito.any(Author.class), Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with blank data")
    public void updateAuthorWithBlankData() {
        Author blankAuthor = AuthorGenerator.createBlankAuthor();
        String json = gson.toJson(blankAuthor);

        Mockito.when(controller.update(Mockito.any(Author.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update existing author with empty data")
    public void updateAuthorWithEmptyData() {
        Author emptyAuthor = AuthorGenerator.createEmptyAuthor();
        String json = gson.toJson(emptyAuthor);

        Mockito.when(controller.update(Mockito.any(Author.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update not existing author with incorrect data")
    public void updateNotExistingAuthorWithIncorrectData() {
        Author blankAuthor = AuthorGenerator.createBlankAuthor();
        String json = gson.toJson(blankAuthor);

        Mockito.when(controller.update(Mockito.any(Author.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("PUT - update not existing author with correct data")
    public void updateNotExistingAuthorWithCorrectData() {
        Author completeAuthor = AuthorGenerator.createCompleteAuthor();
        String json = gson.toJson(completeAuthor);

        Mockito.when(controller.update(Mockito.any(Author.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE existing author")
    public void deleteAuthor() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(delete("/authors/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    @SneakyThrows
    @DisplayName("DELETE not existing author")
    public void deleteNotExistingAuthor() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(delete("/authors/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }
}
