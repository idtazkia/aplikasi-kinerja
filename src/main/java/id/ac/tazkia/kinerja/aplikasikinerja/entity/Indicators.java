package id.ac.tazkia.kinerja.aplikasikinerja.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Indicators {
    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_kpi")
    private Kpi kpi;

    @Column(nullable = false)
    private String score;

    @Column(nullable = false)
    private String content;
}
