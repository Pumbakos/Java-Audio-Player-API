package pl.pumbakos.japwebservice.producermodule;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pumbakos.japwebservice.producermodule.models.Producer;

public interface ProducertRepository extends JpaRepository<Producer, Long> {
}
