package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity @Data
public class StaffKpi {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_kpi")
    private Kpi kpi;

    @OneToMany(mappedBy = "staffKpi", fetch = FetchType.EAGER)
    private List<Evidence> evidence;
}
