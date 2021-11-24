package pl.pumbakos.japwebservice.songmodule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pumbakos.japwebservice.japresources.exception.SongNotFoundException;

@ControllerAdvice
public class SongNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler({SongNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String songNotFoundHandler(SongNotFoundException ex) {
        return ex.getMessage();
    }
}
