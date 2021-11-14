package pl.pumbakos.japwebservice.albummodule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pumbakos.japwebservice.albummodule.models.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
