package pl.pumbakos.japwebservice.songmodule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query(value = "SELECT title FROM Song", nativeQuery = true)
    List<String> findAllByTitle();

    @Query(value = "SELECT size FROM Song song WHERE song.title = :songTitle", nativeQuery = true)
    Optional<Long> findSongSizeByName(@Param("songTitle") String songTitle);

    @Query(value = "SELECT * FROM Song WHERE TITLE = :songTitle", nativeQuery = true)
    Optional<Song> findByTitle(@Param("songTitle") String songTitle);
}
