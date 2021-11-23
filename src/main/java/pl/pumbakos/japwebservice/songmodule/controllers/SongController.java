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

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static pl.pumbakos.japwebservice.japresources.EndPoint.*;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.ID;

@MultipartConfig(maxFileSize = 104857600, maxRequestSize = 1048576000)
@RestController
@RequestMapping(SONGS)
public class SongController {
    private final SongService service;

    @Autowired
    public SongController(SongService service) {
        this.service = service;
    }

    /**
     * Returns file of an WAV extension as a resource to further processing.
     *
     * @param filename song title without extension i.e. if song is named "song.wav" then filename is "song"
     * @return <pre>HttpStatus.OK if song was found successfully.</pre>
     *         <pre>HttpStatus.NOT_FOUND otherwise.</pre>
     * @throws IOException if file is not found on client's side or file is not readable or dur to internal error
     * @see org.springframework.http.HttpStatus
     */
    @SneakyThrows(IOException.class)
    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename) {
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

    //TODO: add playlist functionality
    /**
     * Endpoint is to be updated in future versions to return list of songs for a specific author, album, producer or playlist.
     *
     * Returns list of all song's titles.
     * @return <pre>HttpStatus.OK if songs were found successfully with list of song's titles as body.</pre>
     *         <pre>HttpStatus.NOT_FOUND with message as body otherwise.</pre>
     * @see pl.pumbakos.japwebservice.japresources.Status
     * @see org.springframework.http.HttpStatus
     */
    @GetMapping(path = ALL,
            produces = "application/json")
    public ResponseEntity<String> getTitles() {
        String titles = service.getTitles();
        return titles.isBlank() ?
                new ResponseEntity<>(Status.Message.NOT_FOUND.toString(), HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(titles, HttpStatus.OK);
    }

    /**
     * Returns details for a specific song.
     * 
     * @see Song
     * @param filename song title without extension i.e. if song is named "song.wav" then filename is "song"
     * @return <pre>HttpStatus.OK if song was found successfully with song details as body.</pre>
     *         <pre>HttpStatus.NOT_FOUND otherwise.</pre>
     * @see org.springframework.http.HttpStatus
     */
    @GetMapping(path = INFO,
            produces = "application/json")
    public ResponseEntity<Song> get(@RequestParam("filename") String filename) {
        Song song = service.get(filename);
        return song == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(song, HttpStatus.OK);
    }

    /**
     * Returns list of all song's details.
     * 
     * @return <pre>HttpStatus.OK if songs were found successfully with list of song's details as body</pre>
     *         <pre>HttpStatus.NOT_FOUND otherwise</pre>
     * @see org.springframework.http.HttpStatus
     */
    @GetMapping(path = INFO + ALL,
            produces = "application/json")
    public ResponseEntity<List<Song>> getAll() {
        List<Song> all = service.getAll();
        return all == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(all, HttpStatus.OK);
    }

    /**
     * @deprecated use {@link #get(String filename)} instead and get size from {@link Song#getSize()} <br><br>
     * Endpoint is to be removed in future versions. <br>
     * Return size for a specific song
     * @param filename song title without extension i.e. <br> if song is named "song.wav" then filename is "song"
     *                 <br> if song is named "other song.wav " then filename is "other_song"
     * @return <pre>HttpStatus.OK if song was found successfully with song size as body.</pre>
     *         <pre>HttpStatus.NOT_FOUND otherwise with INVALID_TITLE status code as body.</pre>
     * @see pl.pumbakos.japwebservice.japresources.Status
     * @see org.springframework.http.HttpStatus
     */
    @Deprecated(forRemoval = true)
    @GetMapping(path = SIZE,
            produces = "text/plain")
    public ResponseEntity<Long> getFileSize(@RequestParam("filename") String filename) {
        Long fileSize = service.getFileSize(filename);
        return fileSize == Status.INVALID_TITLE.getCode() ?
                new ResponseEntity<>(Status.INVALID_TITLE.getCode(), HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(fileSize, HttpStatus.OK);
    }

    /**
     * Endpoint is used to upload audio files to the server. Currently, only .wav files are supported.<br><br>
     * Specific key is used to identify whether file is to be uploaded or not. <br>Currently, only "files" is supported
     * but in future versions dynamic key will be used.
     * @param multipartFiles list of files to be uploaded.<br>
     *                       Currently, max size of file is 100 MB and max size of request is 1000 MB
     * @return <pre>HttpStatus.OK if files were uploaded successfully with OK message as body. </pre>
     *         <pre>HttpsStatus.BAD_REQUEST if list of files is empty with BAD_REQUEST message as body </pre>
     *         <pre>HttpStatus.UNPROCESSABLE_ENTITY if files were not uploaded successfully due to bad extension with BAD_EXTENSION message as body </pre>
     *         <pre>HttpStatus.INTERNAL_SERVER_ERROR if files IOException occurred with INTERNAL_ERROR message as body</pre>
     * @see org.springframework.http.HttpStatus
     * @see pl.pumbakos.japwebservice.japresources.Status
     * @see org.springframework.web.multipart.MultipartFile
     * @see java.io.IOException
     */
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
                return new ResponseEntity<>(Status.Message.BAD_EXTENSION.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
            }
            case "INTERNAL_ERROR" -> {
                return new ResponseEntity<>(Status.Message.INTERNAL_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(Status.Message.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Endpoint is used to update specific song's data. <br>
     * Valid Song object is required. <br>
     * @param song valid song object with updated data
     * @param id id of song to be updated
     * @return <pre>HttpStatus.OK if song was updated successfully with UPDATED message as body. </pre>
     *         <pre>HttpStatus.NOT_FOUND if song was not found with NOT_FOUND message as body </pre>
     *
     * @see Song
     * @see Status
     * @see org.springframework.http.HttpStatus
     */
    @PutMapping(path = ID,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> update(@Valid @RequestBody Song song, @PathVariable(name = "id") Long id) {
        return service.update(song, id) ?
                new ResponseEntity<>(Status.Message.UPDATED.toString(), HttpStatus.OK) :
                new ResponseEntity<>(Status.Message.NOT_FOUND.toString(), HttpStatus.NOT_FOUND);
    }
}
