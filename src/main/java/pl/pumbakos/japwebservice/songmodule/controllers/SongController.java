package pl.pumbakos.japwebservice.songmodule.controllers;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pumbakos.japwebservice.japresources.Status;
import pl.pumbakos.japwebservice.songmodule.models.Song;
import pl.pumbakos.japwebservice.songmodule.services.SongService;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static pl.pumbakos.japwebservice.japresources.EndPoint.*;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.FILENAME;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.ID;

@RestController
@RequestMapping(SONGS)
public class SongController {
    private final SongService service;

    @Autowired
    public SongController(SongService service) {
        this.service = service;
    }

    @SneakyThrows
    @GetMapping(path = FILENAME,
            produces = "application/json")
    public ResponseEntity<Resource> download(@PathVariable("filename") String filename) {
        Song song = service.get(filename);
        Path filePath = Path.of(song.getPath());

        Resource resource = service.download(filename);

        if (resource != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("File-Name", filename);
            httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
            httpHeaders.add(CONTENT_TYPE, String.valueOf(
                            MediaType.parseMediaType(Files.probeContentType(filePath))
                    )
            );

//            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
//                    .headers(httpHeaders).body(resource);

            return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = INFO + FILENAME,
            produces = "application/json")
    public ResponseEntity<Song> get(@PathVariable("filename") String filename) {
        Song song = service.get(filename);
        return song == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(song, HttpStatus.OK);
    }

    @Deprecated(forRemoval = true)
    @GetMapping(path = SIZE + FILENAME,
            produces = "text/plain")
    public ResponseEntity<Long> getFileSize(@PathVariable("filename") String filename) {
        Long fileSize = service.getFileSize(filename);
        return fileSize == Status.INVALID_TITLE.getCode() ?
                new ResponseEntity<>(Status.INVALID_TITLE.getCode(), HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(fileSize, HttpStatus.OK);
    }

    @GetMapping(path = ALL,
            produces = "application/json")
    public ResponseEntity<String> getTitles() {
        String titles = service.getTitles();
        return titles.isBlank() ?
                new ResponseEntity<>("NO SONGS FOUND", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping(path = INFO + ALL,
            produces = "application/json")
    public ResponseEntity<List<Song>> getAll() {
        List<Song> all = service.getAll();
        return all == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(all, HttpStatus.OK);
    }

    //FIXME
    @PostMapping(consumes = "multipart/form-data", produces = "text/plain")
    public ResponseEntity<String> upload(@RequestParam("files") List<MultipartFile> multipartFiles) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFiles.get(0).getOriginalFilename()));
        if (multipartFiles.size() == 1 && filename.isBlank()) {
            return new ResponseEntity<>(Status.Message.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
        }

        String message = service.upload(multipartFiles);
        switch (message) {
            case "OK" -> {
                return new ResponseEntity<>(Status.Message.OK.toString(), HttpStatus.OK);
            }
            case "BAD_EXTENSION" -> {
                return new ResponseEntity<>("Bad extension", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            case "INTERNAL_ERROR" -> {
                return new ResponseEntity<>("Error while processing request", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(Status.Message.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = ID,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> update(@Valid @RequestBody Song song, @PathVariable(name = "id") Long id) {
        return service.update(song, id) ?
                new ResponseEntity<>("Song updated successfully", HttpStatus.OK) :
                new ResponseEntity<>("Song not found", HttpStatus.NOT_FOUND);
    }
}
