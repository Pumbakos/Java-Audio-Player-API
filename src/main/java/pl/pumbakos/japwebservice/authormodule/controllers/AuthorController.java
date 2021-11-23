package pl.pumbakos.japwebservice.authormodule.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.authormodule.services.AuthorService;
import pl.pumbakos.japwebservice.producermodule.models.Producer;

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

    /**
     * Returns list of authors.
     * @return <pre>HttpStatus.OK if list is not empty and the list as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if list is empty, null is passed as body</pre>
     * @see HttpStatus
     * @see List
     */
    @GetMapping(path = ALL, produces = "application/json")
    public ResponseEntity<List<Author>> getAll(){
        List<Author> all = service.getAll();
        return all == null ?
                new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(all, HttpStatus.OK);
    }

    /**
     * Returns specific author. Author is identified by id which is passed in path.
     * @param id id of specific author
     * @return <pre>HttpStatus.OK if author is found, author is passed as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if author is not found, null is passed as body</pre>
     * @see HttpStatus
     * @see Author
     */
    @GetMapping(path = ID, produces = "application/json")
    public ResponseEntity<Author> get(@PathVariable(name = "id") Long id){
        Author author = service.get(id);
        return author == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * Creates new author. Author have to be valid.
     * @param author author to be created
     * @return <pre>HttpStatus.OK if author is created, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if author is not created, message is passed as body</pre>
     * @see HttpStatus
     * @see Author
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> save(@Valid @RequestBody Author author){
        //TODO: make it return CREATED status
        return service.save(author) == null ?
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("Author created successfully.", HttpStatus.OK);
    }

    /**
     * Updates author. Author is not required to be valid.
     * @param author updated data of author
     * @param id id of author to be updated
     * @return <pre>HttpStatus.OK if author is updated, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if author is not updated, message is passed as body</pre>
     * @see HttpStatus
     * @see Author
     */
    @PutMapping(path = ID, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> update(@Valid @RequestBody Author author, @PathVariable(name = "id") Long id){
        return service.update(author, id) ?
                new ResponseEntity<>("Updated successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Hard deletes author.
     * @param id id of author to be deleted
     * @return <pre>HttpStatus.OK if author is deleted, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if author is not deleted, message is passed as body</pre>
     * @see HttpStatus
     */
    @DeleteMapping(path = ID,
            produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
        return service.delete(id) ?
                new ResponseEntity<>("Deleted successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Producer not found.", HttpStatus.BAD_REQUEST);
    }
}
