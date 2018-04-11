package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity @Data

public class Score {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_staff")
    private Staff staff;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_kpi")
    private Kpi kpi;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_periode")
    private Periode periode;

    @NotNull @Min(0)
    private BigInteger score;

    @NotNull
    private String remark;


}

