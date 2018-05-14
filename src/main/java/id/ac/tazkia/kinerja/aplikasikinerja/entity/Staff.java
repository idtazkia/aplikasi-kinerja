package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter
@Setter
@EqualsAndHashCode(of = "employeeNumber")
public class Staff {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "employee_name")
    @Size(min = 3, max = 150)
    private String employeeName;

    @NotNull
    @Column(name = "employee_number")
    private String employeeNumber;

    @Column(name = "job_level")
    private String jobLevel;

    @Column(name = "job_title")
    private String jobTitle;

    @Column
    private String department;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "staff_role_staff",
            joinColumns=@JoinColumn(name = "id_staff"),
            inverseJoinColumns = @JoinColumn(name = "id_staff_role"))
    private Set<StaffRole> roles = new HashSet<>();

    @Column
    private String area;

    @OneToOne @JoinColumn(name = "id_user")
    private User user;

    @NotNull
    private String status = "AKTIF";

}
