package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity @Data

public class Score {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_staff_kpi")
    private StaffKpi staffKpi;

    @NotNull
    private String score;

    @NotNull
    private String remark;

    private String total;

    private String employeeComment;

    @OneToMany(mappedBy = "score", fetch = FetchType.EAGER)
    private List<Evidence> evidence;

}

