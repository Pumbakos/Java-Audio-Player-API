package pl.pumbakos.japwebservice.authormodule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.UpdateUtils;
import pl.pumbakos.japwebservice.songmodule.SongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;
    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final UpdateUtils<Author> updateUtils;

    @Autowired
    public AuthorService(AuthorRepository repository, AlbumRepository albumRepository, SongRepository songRepository, UpdateUtils<Author> updateUtils) {
        this.repository = repository;
        this.albumRepository = albumRepository;
        this.songRepository = songRepository;
        this.updateUtils = updateUtils;
    }

    public List<Author>getAll(){
        return repository.findAll();
    }

    public Author get(Long id){
        Optional<Author> optionalAuthor = repository.findById(id);
        return optionalAuthor.orElse(null);
    }

    public Author save(Author author){
        return repository.save(author);
    }

    public boolean update(Author author, Long id){
        return updateUtils.update(repository, author, id);
    }

    public boolean delete(Long id){
        Optional<Author> optionalAuthor = repository.findById(id);
        if(optionalAuthor.isPresent()){
            repository.delete(optionalAuthor.get());
            return true;
        }
        return false;
    }
}
