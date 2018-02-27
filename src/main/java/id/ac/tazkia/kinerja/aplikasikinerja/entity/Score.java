package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity @Data
public class Score {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_staff_kpi")
    private StaffKpi staffKpi;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String score;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String remark;

    @Column(nullable = false)
    @NotEmpty
    @NotNull
    private String total;

    @Column(name = "employee_comments")
    private String employeeComments;

    @Column
    private String evidence;



}
