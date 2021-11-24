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

    /**
     * @return List of all producers
     */
    public List<Producer> getAll() {
        return repository.findAll();
    }

    /**
     * @param id ID of producer to get
     * @return Producer if exists, null otherwise
     */
    public Producer get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ProducerNotFoundException(id));
    }

    /**
     * @param producer Producer to save
     * @return Saved producer
     */
    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    /**
     * Updates producer under given ID.
     *
     * @param producer new producer's data
     * @param id       ID of producer to update
     * @return true if song was updated, false otherwise.
     */
    public boolean update(Producer producer, Long id) {
        return producerUpdateUtils.update(repository, producer, id);
    }

    /**
     * Hard delete of producer.
     *
     * @param id ID of producer to delete
     * @return True if producer was deleted, otherwise throws ProducerNotFoundException
     * @throws ProducerNotFoundException
     * @see ProducerNotFoundException
     */
    public boolean delete(Long id) throws ProducerNotFoundException {
        Optional<Producer> optionalProducer = repository.findById(id);

        if (optionalProducer.isPresent()) {
            repository.delete(optionalProducer.get());
            return true;
        }
        throw new ProducerNotFoundException(id);
    }
}
