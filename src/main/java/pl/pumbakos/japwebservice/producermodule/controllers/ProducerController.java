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

    @GetMapping(path = ALL, produces = "application/json")
    public ResponseEntity<List<Producer>> getAll() {
        List<Producer> all = service.getAll();
        return all == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(all);
    }

    @GetMapping(path = ID, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Producer> get(@Valid @PathVariable(name = "id") Long id) {
        Producer producer = service.get(id);
        return producer == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(producer);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> save(@Valid @RequestBody Producer producer) {
        //TODO: make return CREATED
        return service.save(producer) == null ?
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>("Saved successfully.", HttpStatus.OK);
    }

    @PutMapping(path = ID, consumes = "application/json", produces = "text/plain")
    public ResponseEntity<String> update(@RequestBody Producer producer, @PathVariable(name = "id") Long id) {
        return service.update(producer, id) ?
                new ResponseEntity<>("Updated successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Check data you have entered.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = ID, produces = "text/plain")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id) {
        return service.delete(id) ?
                new ResponseEntity<>("Deleted successfully.", HttpStatus.OK) :
                new ResponseEntity<>("Producer not found.", HttpStatus.NOT_FOUND);
    }
}
