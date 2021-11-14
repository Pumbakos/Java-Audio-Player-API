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

    //FIXME
    @PostMapping(consumes = "multipart/form-data", produces = "text/plain")
    public ResponseEntity<String> upload(@RequestParam("files") List<MultipartFile> multipartFiles) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFiles.get(0).getOriginalFilename()));
        if (multipartFiles.size() == 1 && filename.isBlank()) {
            return ResponseEntity.noContent().build();
        }

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Type", "multipart/form-data");

        String message = service.upload(multipartFiles);
        switch (message) {
            case "OK"-> {
                return ResponseEntity.ok(Status.Message.OK.toString());
            }
            case "BAD_EXTENSION"-> {
                return ResponseEntity.unprocessableEntity().build();
            }
            case "NO_CONTENT" -> {
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = ID,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> update(@Valid @RequestBody Song song, @PathVariable(name = "id") Long id) {
        return service.update(song, id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.notFound().build();
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

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .headers(httpHeaders).body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = INFO + FILENAME,
            produces = "application/json")
    public ResponseEntity<Song> get(@PathVariable("filename") String filename) {
        Song song = service.get(filename);
        return song == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(song);
    }

    @GetMapping(path = SIZE + FILENAME,
            produces = "text/plain")
    public ResponseEntity<Long> getFileSize(@PathVariable("filename") String filename) {
        Long fileSize = service.getFileSize(filename);
        if (fileSize == Status.INVALID_TITLE.getCode())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(fileSize);
    }

    @GetMapping(path = ALL,
            produces = "application/json")
    public ResponseEntity<String> getTitles() {
        String titles = service.getTitles();
        return titles.isBlank() ? ResponseEntity.noContent().build() : ResponseEntity.ok(titles);
    }

    @GetMapping(path = INFO + ALL,
            produces = "application/json")
    public ResponseEntity<List<Song>> getAll() {
        List<Song> all = service.getAll();
        return all == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(all);
    }
}
