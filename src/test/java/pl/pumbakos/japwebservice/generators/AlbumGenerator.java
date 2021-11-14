package pl.pumbakos.japwebservice.generators;

import pl.pumbakos.japwebservice.albummodule.models.Album;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AlbumGenerator {
    public static Album createCompleteAlbum(){
        Album album = new Album();
        album.setName("tonight");
        album.setDescription("anyone");
        album.setProducer(ProducerGenerator.createCompleteProducer());
        album.setReleaseDate(Date.valueOf(LocalDate.now()));

        return album;
    }

    public static Album createAnotherCompleteAlbum(){
        Album album = new Album();
        album.setName("profit");
        album.setDescription("wreck");
        album.setProducer(ProducerGenerator.createAnotherCompleteProducer());
        album.setReleaseDate(Date.valueOf(LocalDate.now()));

        return album;
    }

    public static Album createBlankAlbum(){return new Album();}
}
