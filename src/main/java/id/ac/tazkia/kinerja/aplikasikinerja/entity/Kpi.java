package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

@Entity @Data
public class Kpi {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(name = "key_result",nullable = false)
    @NotNull
    @NotEmpty
    private String keyResult;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String weight;

    @Column(name = "base_line",nullable = false)
    private String baseLine;

    @Column(nullable = false)
    private String target;

    @OneToMany(mappedBy = "kpi")
    private List<Indicators> indicatorsList = new ArrayList<>();

    @NotNull @Enumerated(EnumType.STRING)
    private StatusKpi status= StatusKpi.AKTIF;

}
