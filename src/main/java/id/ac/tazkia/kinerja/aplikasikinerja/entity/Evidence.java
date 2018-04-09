package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data @Entity
public class Evidence {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn
    private Staff staff;

    @ManyToOne
    @JoinColumn
    private Kpi kpi;

    @ManyToOne
    @JoinColumn
    private Periode periode;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String description;


}
