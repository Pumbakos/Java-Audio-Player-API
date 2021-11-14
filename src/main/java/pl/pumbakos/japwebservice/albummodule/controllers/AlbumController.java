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

    @GetMapping(path = ALL,
            produces = "application/json")
    public ResponseEntity<List<Album>> getAll() {
        List<Album> all = service.getAll();
        return all == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(all);
    }

    @GetMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<Album> get(@PathVariable(name = "id") Long id) {
        Album album = service.get(id);
        return album == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(album);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity.BodyBuilder save(@Valid @RequestBody Album album) {
        service.save(album);
        return ResponseEntity.status(HttpStatus.CREATED);
    }

    @PutMapping(path = ID,
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> update(@Valid @RequestBody Album album, @PathVariable(name = "id") Long id) {
        return service.update(album, id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "id") Long id) {
        return service.delete(id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }
}
