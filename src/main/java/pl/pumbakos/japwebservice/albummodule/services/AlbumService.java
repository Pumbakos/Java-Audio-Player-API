package pl.pumbakos.japwebservice.albummodule.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.DefaultUtils;
import pl.pumbakos.japwebservice.producermodule.ProducertRepository;
import pl.pumbakos.japwebservice.songmodule.SongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final AuthorRepository authorRepository;
    private final ProducertRepository producertRepository;
    private final DefaultUtils<Album> defaultUtils;

    @Autowired
    public AlbumService(AlbumRepository repository, AuthorRepository authorRepository, SongRepository songRepository, ProducertRepository producertRepository, DefaultUtils<Album> defaultUtils) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.producertRepository = producertRepository;
        this.defaultUtils = defaultUtils;
    }

    public List<Album> getAll() {
        return repository.findAll();
    }

    public Album get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SneakyThrows
    public Album save(Album album) {
        defaultUtils.checkIfPresent(producertRepository, album.getProducer());
        defaultUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return repository.save(album);
    }

    @SneakyThrows
    public boolean update(Album album, Long id) {
        defaultUtils.checkIfPresent(producertRepository, album.getProducer());
        defaultUtils.checkIfPresents(authorRepository, album.getAuthors(), Author.class);

        return defaultUtils.update(repository, album, id);
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
