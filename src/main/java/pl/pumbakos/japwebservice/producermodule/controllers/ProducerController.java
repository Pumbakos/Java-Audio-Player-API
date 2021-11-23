package pl.pumbakos.japwebservice.producermodule.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pumbakos.japwebservice.producermodule.models.Producer;
import pl.pumbakos.japwebservice.producermodule.services.ProducerService;

import javax.validation.Valid;
import java.util.List;

import static pl.pumbakos.japwebservice.japresources.EndPoint.ALL;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PRODUCERS;
import static pl.pumbakos.japwebservice.japresources.EndPoint.PathVariable.ID;

@RestController
@RequestMapping(path = PRODUCERS)
public class ProducerController {
    private final ProducerService service;

    @Autowired
    public ProducerController(ProducerService service) {
        this.service = service;
    }

    /**
     * Returns list of producers.
     * @return <pre>HttpStatus.OK if list is not empty and the list as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if list is empty, null as body is returned</pre>
     * @see HttpStatus
     * @see List
     */
    @GetMapping(path = ALL, produces = "application/json")
    public ResponseEntity<List<Producer>> getAll() {
        List<Producer> all = service.getAll();
        return all == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(all, HttpStatus.OK);
    }

    /**
     * Returns specific producer. Producer is identified by id which is passed in path.
     * @param id id of specific producer
     * @return <pre>HttpStatus.OK if producer is found, producer is passed as body</pre>
     *         <pre>HttpStatus.NOT_FOUND if producer is not found, null is passed as body</pre>
     * @see HttpStatus
     * @see Producer
     */
    @GetMapping(path = ID, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Producer> get(@Valid @PathVariable(name = "id") Long id) {
        Producer producer = service.get(id);
        return producer == null ?
                new ResponseEntity<>(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(producer, HttpStatus.OK);
    }

    /**
     * Creates new producer. Producer have to be valid.
     * @param producer producer to be created
     * @return <pre>HttpStatus.OK if producer is created, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if producer is not created, message is passed as body</pre>
     * @see HttpStatus
     * @see Producer
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> save(@Valid @RequestBody Producer producer) {
        //TODO: make return CREATED
        return service.save(producer) == null ?
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("Saved successfully.", HttpStatus.OK);
    }

    /**
     * Updates producer. Producer is not required to be valid.
     * @param producer updated data of producer
     * @param id id of producer to be updated
     * @return <pre>HttpStatus.OK if producer is updated, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if producer is not updated, message is passed as body</pre>
     * @see HttpStatus
     * @see Producer
     */
    @PutMapping(path = ID, consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> update(@RequestBody Producer producer, @PathVariable(name = "id") Long id) {
        return service.update(producer, id) ?
                new ResponseEntity<>("Updated successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Hard deletes producer.
     * @param id id of producer to be deleted
     * @return <pre>HttpStatus.OK if producer is deleted, message is passed as body</pre>
     *         <pre>HttpStatus.BAD_REQUEST if producer is not deleted, message is passed as body</pre>
     * @see HttpStatus
     */
    @DeleteMapping(path = ID, produces = "text/plain")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        return service.delete(id) ?
                new ResponseEntity<>("Deleted successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Producer not found.", HttpStatus.BAD_REQUEST);
    }
}
