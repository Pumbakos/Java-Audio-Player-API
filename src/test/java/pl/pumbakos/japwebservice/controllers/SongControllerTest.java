package pl.pumbakos.japwebservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import pl.pumbakos.japwebservice.songmodule.controllers.SongController;

@WebMvcTest(controllers = SongController.class)
public class SongControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Gson gson = new Gson();

}
