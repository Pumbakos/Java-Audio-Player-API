package pl.pumbakos.japwebservice.producermodule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.pumbakos.japwebservice.albummodule.models.Album;
import pl.pumbakos.japwebservice.japresources.DBModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
@Setter
@Getter
@ToString
public class Producer extends DBModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    @Column(name = "id", columnDefinition = "INT", unique = true, updatable = false)
    private Long id;

    @NotBlank(message = "We require name not to be empty, check data you entered")
    @Column(nullable = false, name = "name", columnDefinition = "VARCHAR(50)")
    private String name;

    @NotBlank(message = "We require surname not to be empty, check data you entered")
    @Column(nullable = false, name = "surname", columnDefinition = "VARCHAR(50)")
    private String surname;

    @Column(name = "nickname", columnDefinition = "VARCHAR(50)")
    private String nickname;

    @OneToMany(mappedBy = "producer")
    @JsonIgnore
    @ToString.Exclude
    private List<Album> albums;
}
