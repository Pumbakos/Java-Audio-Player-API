package pl.pumbakos.japwebservice.albummodule.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.albummodule.services.AlbumService;

import javax.validation.Valid;
import java.util.List;

import static pl.pumbakos.japwebservice.japresources.EndPoint.ALBUMS;
import static pl.pumbakos.japwebservice.japresources.EndPoint.ALL;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.ID;

@RestController
@RequestMapping(path = ALBUMS)
public class AlbumController {
    private final AlbumService service;

    @Autowired
    public AlbumController(AlbumService service) {
        this.service = service;
    }

    /**
     * Returns list of albums.
     * @return <pre>HttpStatus.OK if list is not empty and the list as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if list is empty, null is passed as body</pre>
     * @see HttpStatus
     * @see List
     */
    @GetMapping(path = ALL,
            produces = "application/json")
    public ResponseEntity<List<Album>> getAll() {
        List<Album> all = service.getAll();
        return all == null ?
                new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(all, HttpStatus.OK);
    }

    /**
     * Returns specific album. Album is identified by id which is passed in path.
     * @param id id of specific album
     * @return <pre>HttpStatus.OK if album is found, album is passed as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if album is not found, null is passed as body</pre>
     * @see HttpStatus
     * @see Album
     */
    @GetMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<Album> get(@PathVariable(name = "id") Long id) {
        Album album = service.get(id);
        return album == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(album, HttpStatus.OK);
    }

    /**
     * Creates new album. Album have to be valid.
     * @param album album to be created
     * @return <pre>HttpStatus.OK if album is created, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if album is not created, message is passed as body</pre>
     * @see HttpStatus
     * @see Album
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> save(@Valid @RequestBody Album album) {
        return service.save(album) == null ?
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("Album created successfully.", HttpStatus.OK);
    }

    /**
     * Updates album. Album is not required to be valid.
     * @param album updated data of album
     * @param id id of album to be updated
     * @return <pre>HttpStatus.OK if album is updated, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if album is not updated, message is passed as body</pre>
     * @see HttpStatus
     * @see Album
     */
    @PutMapping(path = ID,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> update(@Valid @RequestBody Album album, @PathVariable(name = "id") Long id) {
        return service.update(album, id) ?
                new ResponseEntity<>("Updated successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Hard deletes album.
     * @param id id of album to be deleted
     * @return <pre>HttpStatus.OK if album is deleted, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if album is not deleted, message is passed as body</pre>
     * @see HttpStatus
     */
    @DeleteMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        return service.delete(id) ?
                new ResponseEntity<>("Deleted successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Producer not found.", HttpStatus.NOT_FOUND);
    }
}
