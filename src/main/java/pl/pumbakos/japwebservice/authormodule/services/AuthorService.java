package pl.pumbakos.japwebservice.authormodule.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.UpdateUtils;
import pl.pumbakos.japwebservice.japresources.exception.AuthorNotFoundException;
import pl.pumbakos.japwebservice.songmodule.SongRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository repository;
    private final UpdateUtils<Author> updateUtils;

    @Autowired
    public AuthorService(AuthorRepository repository, UpdateUtils<Author> updateUtils) {
        this.repository = repository;
        this.updateUtils = updateUtils;
    }

    /**
     * @return List of all authors
     */
    public List<Author>getAll(){
        return repository.findAll();
    }

    /**
     * @param id ID of author to get
     * @return Author if exists, null otherwise
     */
    public Author get(Long id){
        Optional<Author> optionalAuthor = repository.findById(id);
        return optionalAuthor.orElse(null);
    }

    /**
     * @param author Author to save
     * @return Saved album
     */
    public Author save(Author author){
        return repository.save(author);
    }

    /**
     * Updates author under given ID.
     * @param author new author's data
     * @param id ID of author to update
     * @return true if song was updated, false otherwise.
     */
    public boolean update(Author author, Long id){
        return updateUtils.update(repository, author, id);
    }

    /**
     * Hard delete of author.
     * @param id ID of author to delete
     * @return true if author was deleted, false otherwise
     */
    public boolean delete(Long id){
        Optional<Author> optionalAuthor = repository.findById(id);
        if(optionalAuthor.isPresent()){
            repository.delete(optionalAuthor.get());
            return true;
        }
        throw new AuthorNotFoundException(id);
    }
}
