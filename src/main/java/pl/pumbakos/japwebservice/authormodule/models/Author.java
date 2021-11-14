package pl.pumbakos.japwebservice.authormodule.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.japresources.DBModel;
import pl.pumbakos.japwebservice.japresources.DateFormat;
import pl.pumbakos.japwebservice.japresources.JAPDate;
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
public class Author extends DBModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    @Column(name = "author_id", columnDefinition = "INT", unique = true)
    private Long id;

    @NotBlank
    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @NotBlank
    @Column(nullable = false, name = "surname", columnDefinition = "VARCHAR(50)")
    private String surname;

    @Basic
    @Column(name = "nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    @ToString.Exclude
    private List<Album> albums;

    @NotNull
    @Column(nullable = false, name = "date_of_birth", columnDefinition = "DATETIME")
    @JsonFormat(pattern = DateFormat.ISO)
    private Date dateOfBirth;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    @ToString.Exclude
    private List<Song> songs;
}
