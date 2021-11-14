package pl.pumbakos.japwebservice.authormodule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pumbakos.japwebservice.authormodule.models.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
