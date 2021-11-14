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
import pl.pumbakos.japwebservice.generators.ProducerGenerator;
import pl.pumbakos.japwebservice.producermodule.controllers.ProducerController;
import pl.pumbakos.japwebservice.producermodule.models.Producer;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static pl.pumbakos.japwebservice.japresources.DateFormat.ISO;

@WebMvcTest(controllers = {ProducerController.class})
public class ProducerControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new GsonBuilder().setDateFormat(ISO).create();

    @MockBean
    private ProducerController controller;

    @Autowired
    private MockMvc mock;

    @SneakyThrows
    @Test
    @DisplayName("GET all producers")
    public void getAllProducers() {
        Producer completeProducer = ProducerGenerator.createCompleteProducer();
        Producer anotherCompleteProducer = ProducerGenerator.createAnotherCompleteProducer();

        List<Producer> producers = Arrays.asList(completeProducer, anotherCompleteProducer);

        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.ok(producers));

        String contentAsString = mock.perform(get("/producers/all"))
                .andReturn().getResponse().getContentAsString();

        List<Producer> resultVacation = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        Assertions.assertEquals(completeProducer.getId(), resultVacation.get(0).getId());
        Assertions.assertEquals(anotherCompleteProducer.getId(), resultVacation.get(1).getId());
    }

    @SneakyThrows
    @Test
    @DisplayName("GET empty producers list")
    public void getAllEmptyProducersList() {
        Mockito.when(controller.getAll()).thenReturn(ResponseEntity.noContent().build());

        int status = mock.perform(get("/producers/all"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("GET not existing producer")
    public void getNotExistingProducer() {
        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(get("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("GET existing producer")
    public void getExistingProducer() {
        Producer completeProducer = ProducerGenerator.createCompleteProducer();

        Mockito.when(controller.get(Mockito.anyLong())).thenReturn(ResponseEntity.ok(completeProducer));

        int status = mock.perform(get("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("POST blank producer")
    public void postBlankProducer() {
        Producer blankProducer = ProducerGenerator.createBlankProducer();
        String json = gson.toJson(blankProducer);

        Mockito.when(controller.save(blankProducer)).thenReturn(ResponseEntity.badRequest().build());

        int responseStatusCode = mock.perform(post("/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatusCode);
    }

    @SneakyThrows
    @Test
    @DisplayName("POST empty producer")
    public void postEmptyProducer() {
        Producer emptyProducer = ProducerGenerator.createEmptyProducer();
        String json = gson.toJson(emptyProducer);

        Mockito.when(controller.save(emptyProducer)).thenReturn(ResponseEntity.badRequest().build());

        int responseStatusCode = mock.perform(post("/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatusCode);
    }

    @SneakyThrows
    @Test
    @DisplayName("POST new producer")
    public void postCompleteProducer() {
        Producer completeProducer = ProducerGenerator.createCompleteProducer();
        String json = gson.toJson(completeProducer);

        Mockito.when(controller.save(completeProducer)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        int responseStatusCode = mock.perform(post("/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), responseStatusCode);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT - update existing producer with blank data")
    public void updateWithBlankData() {
        Producer blankProducer = ProducerGenerator.createBlankProducer();
        String json = gson.toJson(blankProducer);

        Mockito.when(controller.update(Mockito.any(Producer.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT - update existing producer with empty data")
    public void updateWithEmptyData() {
        Producer emptyProducer = ProducerGenerator.createEmptyProducer();
        String json = gson.toJson(emptyProducer);

        Mockito.when(controller.update(Mockito.any(Producer.class), Mockito.anyLong())).thenReturn(ResponseEntity.badRequest().build());

        int status = mock.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT - update existing producer with correct data")
    public void updateWithCorrectData() {
        Producer completeProducer = ProducerGenerator.createCompleteProducer();
        String json = gson.toJson(completeProducer);

        Mockito.when(controller.update(Mockito.any(Producer.class), Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT - update not existing producer with empty data")
    public void updateNotExistingWithEmptyData() {
        Producer emptyProducer = ProducerGenerator.createEmptyProducer();
        String json = gson.toJson(emptyProducer);

        Mockito.when(controller.update(Mockito.any(Producer.class), Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("PUT - update not existing producer with correct data")
    public void updateExistingWithCorrectData() {
        Producer completeProducer = ProducerGenerator.createCompleteProducer();
        String json = gson.toJson(completeProducer);

        Mockito.when(controller.update(Mockito.any(Producer.class), Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("DELETE not existing producer")
    public void deleteNotExistingProducer() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

        int status = mock.perform(delete("/producers/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @SneakyThrows
    @Test
    @DisplayName("DELETE existing producer")
    public void deleteExistingProducer() {
        Mockito.when(controller.delete(Mockito.anyLong())).thenReturn(ResponseEntity.ok().build());

        int status = mock.perform(delete("/producers/1"))
                .andReturn().getResponse().getStatus();

        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }
}
