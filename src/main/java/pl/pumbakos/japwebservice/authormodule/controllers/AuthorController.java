package pl.pumbakos.japwebservice.authormodule.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.authormodule.services.AuthorService;

import javax.validation.Valid;
import java.util.List;

import static pl.pumbakos.japwebservice.japresources.EndPoint.ALL;
import static pl.pumbakos.japwebservice.japresources.EndPoint.AUTHORS;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.ID;

@RestController
@RequestMapping(AUTHORS)
public class AuthorController {
    private final AuthorService service;

    @Autowired
    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping(path = ALL, produces = "application/json")
    public ResponseEntity<List<Author>> getAll(){
        List<Author> all = service.getAll();
        return all == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(all);
    }

    @GetMapping(path = ID, produces = "application/json")
    public ResponseEntity<Author> get(@PathVariable(name = "id") Long id){
        Author author = service.get(id);
        return author == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(author);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> save(@Valid @RequestBody Author author){
        //TODO: make it return CREATED status
        return service.save(author) == null ?
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("Author created successfully.", HttpStatus.OK);
    }

    @PutMapping(path = ID, consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> update(@Valid @RequestBody Author author, @PathVariable(name = "id") Long id){
        return service.update(author, id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "id") Long id){
        return service.delete(id) ? ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.notFound().build();
    }
}
