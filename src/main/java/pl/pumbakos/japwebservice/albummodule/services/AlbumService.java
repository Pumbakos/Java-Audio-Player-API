package pl.pumbakos.japwebservice.albummodule.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.UpdateUtils;
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
    public AlbumService(AlbumRepository repository, AuthorRepository authorRepository, SongRepository songRepository, ProducertRepository producertRepository, UpdateUtils<Album> updateUtils) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.producertRepository = producertRepository;
        this.updateUtils = updateUtils;
    }

    public List<Album> getAll() {
        return repository.findAll();
    }

    public Album get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SneakyThrows
    public Album save(Album album) {
        updateUtils.checkIfPresent(producertRepository, album.getProducer());
        updateUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return repository.save(album);
    }

    @SneakyThrows
    public boolean update(Album album, Long id) {
        updateUtils.checkIfPresent(producertRepository, album.getProducer());
        updateUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return updateUtils.update(repository, album, id);
    }

    public boolean delete(Long id) {
        Optional<Album> optionalAlbum = repository.findById(id);
        if (optionalAlbum.isPresent()) {
            repository.delete(optionalAlbum.get());
            return true;
        }
        return false;
    }
}
