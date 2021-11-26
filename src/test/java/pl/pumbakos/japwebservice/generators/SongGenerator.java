package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.util.List;

public class SongGenerator {
    public static Song createCompleteSong(){
        Song song = new Song();
        song.setTitle("to ja");
        song.setExtension(".wav");
        song.setPath("C:\\Users\\Pumbakos\\Downloads");
        song.setSize(18075982475L);
        song.setAuthors(List.of(AuthorGenerator.createCompleteAuthor(), AuthorGenerator.createAnotherCompleteAuthor()));
        song.setAlbum(AlbumGenerator.createCompleteAlbum());

        return song;
    }

    public static Song createAnotherCompleteSong(){
        Song song = new Song();
        song.setTitle("bandyta");
        song.setExtension(".wav");
        song.setPath("C:\\Users\\Pumbakos\\Downloads");
        song.setSize(18075982475L);
        song.setAuthors(List.of(AuthorGenerator.createCompleteAuthor(), AuthorGenerator.createAnotherCompleteAuthor()));
        song.setAlbum(AlbumGenerator.createAnotherCompleteAlbum());

        return song;
    }

    public static Song createEmptySong(){
        Song song = new Song();
        song.setTitle("");
        song.setPath("");
        song.setExtension("");
        song.setAuthors(List.of());
        song.setAlbum(new Album());

        return song;
    }
}
