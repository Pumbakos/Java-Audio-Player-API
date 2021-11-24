package pl.pumbakos.japwebservice.producermodule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.japresources.UpdateUtils;
import pl.pumbakos.japwebservice.japresources.exception.ProducerNotFoundException;
import pl.pumbakos.japwebservice.producermodule.ProducertRepository;
import pl.pumbakos.japwebservice.producermodule.models.Producer;

import java.util.List;
import java.util.Optional;

@Service
public class ProducerService {
    private final ProducertRepository repository;
    private final UpdateUtils<Producer> producerUpdateUtils;

    @Autowired
    public ProducerService(ProducertRepository repository, UpdateUtils<Producer> producerUpdateUtils) {
        this.repository = repository;
        this.producerUpdateUtils = producerUpdateUtils;
    }

    public List<Producer> getAll() {
        return repository.findAll();
    }

    public Producer get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProducerNotFoundException(id));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public boolean update(Producer producer, Long id) {
        return producerUpdateUtils.update(repository, producer, id);
    }

    public boolean delete(Long id) {
        Optional<Producer> byId = repository.findById(id);
        if(byId.isPresent()){
            repository.delete(byId.get());
            return true;
        }
        throw new ProducerNotFoundException(id);
    }
}
