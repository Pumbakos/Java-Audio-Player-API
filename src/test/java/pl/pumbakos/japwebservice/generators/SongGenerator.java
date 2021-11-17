package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.songmodule.models.Song;

import java.util.List;

public class SongGenerator {
    public static Song createCompleteSong(){
        Song song = new Song();
        song.setTitle("to ja");
        song.setPath("");
        song.setSize(18075982475L);
        song.setAuthors(List.of(AuthorGenerator.createCompleteAuthor(), AuthorGenerator.createAnotherCompleteAuthor()));
        song.setAlbum(AlbumGenerator.createCompleteAlbum());

        return song;
    }

    public static Song createAnotherCompleteSong(){
        Song song = new Song();
        song.setTitle("bandyta");
        song.setPath("");
        song.setSize(18075982475L);
        song.setAuthors(List.of(AuthorGenerator.createCompleteAuthor(), AuthorGenerator.createAnotherCompleteAuthor()));
        song.setAlbum(AlbumGenerator.createAnotherCompleteAlbum());

        return song;
    }

    public static Song createBlankSong(){
        return new Song();
    }

    public static Song createEmptySong(){
        Song song = new Song();
        song.setTitle("");
        song.setPath("");
        song.setAuthors(List.of());

        return song;
    }
}