package pl.pumbakos.japwebservice.songmodule.services;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.pumbakos.japwebservice.albummodule.AlbumRepository;
import pl.pumbakos.japwebservice.authormodule.AuthorRepository;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.DefaultUtils;
import pl.pumbakos.japwebservice.japresources.Extension;
import pl.pumbakos.japwebservice.japresources.Status;
import pl.pumbakos.japwebservice.producermodule.ProducertRepository;
import pl.pumbakos.japwebservice.songmodule.SongRepository;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class SongService {
    private static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/";
    private final SongRepository repository;
    private final AlbumRepository albumRepository;
    private final AuthorRepository authorRepository;
    private final ProducertRepository producerRepository;
    private final DefaultUtils<Song> defaultUtils;
    private final Gson gson;

    @Autowired
    public SongService(SongRepository repository, AlbumRepository albumRepository, AuthorRepository authorRepository,
                       ProducertRepository producerRepository, Gson gson, DefaultUtils<Song> defaultUtils) {
        this.repository = repository;
        this.albumRepository = albumRepository;
        this.authorRepository = authorRepository;
        this.producerRepository = producerRepository;
        this.gson = gson;
        this.defaultUtils = defaultUtils;
    }

    public String upload(List<MultipartFile> multipartFiles) {
        try {
            for (MultipartFile file : multipartFiles) {
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                if (!filename.endsWith(Extension.WAV)) {
                    return Status.Message.BAD_EXTENSION.toString();
                }
                Song song = new Song();
                song.setPath(Paths.get(DIRECTORY, filename).toAbsolutePath().normalize().toString());

                Path fileStorage = Path.of(song.getPath());
                copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);

                long size = file.getSize();
                String title = filename.substring(0, filename.length() - 4);

                song.setSize(size);
                song.setTitle(title);

                repository.save(song);
            }
            return Status.Message.OK.toString();
        } catch (IOException e) {
            return Status.Message.NO_CONTENT.toString();
        }
    }

    @SneakyThrows
    public boolean update(Song song, Long id) {
        defaultUtils.checkIfPresents(authorRepository, song.getAuthors(), Author.class);
        defaultUtils.checkIfPresent(albumRepository, song.getAlbum());
        defaultUtils.checkIfPresent(producerRepository, song.getAlbum().getProducer());

        return defaultUtils.update(repository, song, id);
    }

    public Resource download(String filename) {
        Optional<Song> optionalSong = repository.findByTitle(filename);

        Path filePath;
        try {
            if (optionalSong.isPresent())
                filePath = Path.of(optionalSong.get().getPath());
            else
                return null;

            if (!Files.exists(filePath)) {
                return null;
            }

            return new UrlResource(filePath.toUri());
        } catch (InvalidPathException | IOException e) {
            return null;
        }
    }

    public Long getFileSize(String filename) {
        String trimmedFilename = filename.replace('_', ' ');
        Optional<Long> songSizeByName = repository.findSongSizeByName(trimmedFilename);
        return songSizeByName.orElseGet(Status.INVALID_TITLE::getCode);
    }

    public String getTitles() {
        return gson.toJson(repository.findAllByTitle());
    }

    public Song get(String filename) {
        Optional<Song> optionalSong = repository.findByTitle(filename);
        return optionalSong.orElse(null);
    }

    public List<Song> getAll() {
        return repository.findAll();
    }
}
