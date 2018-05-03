package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class ScoreComment {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    private String content;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "author")
    private Staff author;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "score")
    private Score score;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "periode")
    private Periode periode;

    @NotNull
    private LocalDateTime postingTime = LocalDateTime.now();
}
