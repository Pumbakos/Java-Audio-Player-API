package pl.pumbakos.japwebservice.albummodule.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pumbakos.japwebservice.authormodule.models.Author;
import pl.pumbakos.japwebservice.japresources.DBModel;
import pl.pumbakos.japwebservice.producermodule.models.Producer;
import pl.pumbakos.japwebservice.songmodule.models.Song;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@ToString
public class Album extends DBModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    @Column(name = "album_id", columnDefinition = "INT", unique = true)
    private Long id;

    @NotBlank
    @Column(nullable = false, name = "title", columnDefinition = "VARCHAR(50)")
    private String title;

    @NotBlank
    @Column(nullable = false, name = "description", columnDefinition = "VARCHAR(200)")
    private String description;

    @NotNull
    @Column(nullable = false, name = "release_date", columnDefinition = "DATETIME")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date releaseDate;

    @Column(nullable = false)
    @OneToMany(mappedBy = "album")
    @JsonIgnore
    @ToString.Exclude
    private List<Song> songs;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "AUTHOR_ALBUM",
            joinColumns = {@JoinColumn(name = "author_id")},
            inverseJoinColumns = {@JoinColumn(name = "album_id")}
    )
    @ToString.Exclude
    private List<Author> authors;

    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Producer producer;
}
