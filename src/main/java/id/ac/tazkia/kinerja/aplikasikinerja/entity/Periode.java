package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
public class Periode {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    private String periodeName;

    @NotNull @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @NotNull @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @NotNull
    private String active;

    @NotNull
    private String description;
}
