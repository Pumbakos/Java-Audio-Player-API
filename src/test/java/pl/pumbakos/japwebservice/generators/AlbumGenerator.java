package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.japresources.JAPDate;

import java.sql.Date;
import java.time.LocalDate;

public class AlbumGenerator {
    public static Album createCompleteAlbum(){
        Album album = new Album();
        album.setTitle("tonight");
        album.setDescription("anyone");
        album.setProducer(ProducerGenerator.createCompleteProducer());
        album.setReleaseDate(Date.valueOf(LocalDate.now()));

        return album;
    }

    public static Album createAnotherCompleteAlbum(){
        Album album = new Album();
        album.setTitle("profit");
        album.setDescription("wreck");
        album.setProducer(ProducerGenerator.createAnotherCompleteProducer());
        album.setReleaseDate(Date.valueOf(LocalDate.now()));

        return album;
    }

    public static Album createBlankAlbum(){return new Album();}

    public static Album createEmptyAlbum(){
        Album album = new Album();
        album.setTitle("");
        album.setDescription("");
        album.setReleaseDate(JAPDate.of(""));

        return album;
    }
}
