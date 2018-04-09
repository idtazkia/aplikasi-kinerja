package id.ac.tazkia.kinerja.aplikasikinerja.entity;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

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

    @NotNull @Min(0)
    private BigInteger score;

    @Column(nullable = false)
    private String content;
}
