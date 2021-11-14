package pl.pumbakos.japwebservice.songmodule.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.DBModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@ToString
public class Song extends DBModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    @Column(name = "song_id", columnDefinition = "INT", unique = true, updatable = false, insertable = false)
    private Long id;

    @NotBlank(message = "We require title not to be empty, check data you entered")
    @Column(nullable = false, name = "title", columnDefinition = "VARCHAR(255)")
    private String title;

    @NotNull(message = "We require size not to be empty, check data you entered")
    @Column(nullable = false, name = "size", columnDefinition = "INT")
    private Long size;

    @NotNull
    @Column(nullable = false, name = "path", columnDefinition = "VARCHAR(MAX)")
    private String path;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "SONG_AUTHOR",
            joinColumns = {@JoinColumn(name = "author_id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id")}
    )
    @ToString.Exclude
    private List<Author> authors;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
}
