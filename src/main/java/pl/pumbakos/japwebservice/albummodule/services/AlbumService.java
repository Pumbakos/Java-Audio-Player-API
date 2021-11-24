package pl.pumbakos.japwebservice.albummodule.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.UpdateUtils;
import pl.pumbakos.japwebservice.japresources.exception.AlbumNotFoundException;
import pl.pumbakos.japwebservice.producermodule.ProducertRepository;
import pl.pumbakos.japwebservice.songmodule.SongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final AuthorRepository authorRepository;
    private final ProducertRepository producertRepository;
    private final UpdateUtils<Album> updateUtils;

    @Autowired
    public AlbumService(AlbumRepository repository, AuthorRepository authorRepository, ProducertRepository producertRepository, UpdateUtils<Album> updateUtils) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.producertRepository = producertRepository;
        this.updateUtils = updateUtils;
    }

    /**
     * @return List of all albums
     */
    public List<Album> getAll() {
        return repository.findAll();
    }

    /**
     * @param id ID of album to get
     * @return Album if exists, null otherwise
     */
    public Album get(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * @param album Album to save
     * @return Saved album
     */
    @SneakyThrows
    public Album save(Album album) {
        updateUtils.checkIfPresent(producertRepository, album.getProducer());
        updateUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return repository.save(album);
    }

    /**
     * Updates album under given ID.
     * @param album new album's data
     * @param id ID of album to update
     * @return true if song was updated, false otherwise.
     */
    @SneakyThrows
    public boolean update(Album album, Long id) {
        updateUtils.checkIfPresent(producertRepository, album.getProducer());
        updateUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return updateUtils.update(repository, album, id);
    }

    /**
     * Hard delete of album.
     * @param id ID of album to delete
     * @return True if album was deleted, false otherwise
     */
    public boolean delete(Long id) {
        Optional<Album> optionalAlbum = repository.findById(id);
        if (optionalAlbum.isPresent()) {
            repository.delete(optionalAlbum.get());
            return true;
        }
        throw new AlbumNotFoundException(id);
    }
}
