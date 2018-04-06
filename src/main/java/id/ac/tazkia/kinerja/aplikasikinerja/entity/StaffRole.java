package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = "roleName")
public class StaffRole {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "role_name", nullable = false)
    @NotEmpty
    private String roleName;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(name = "staff_role_kpi",
               joinColumns=@JoinColumn(name = "id_staff_role"),
               inverseJoinColumns = @JoinColumn(name = "id_kpi"))
    private Set<Kpi> kpi = new HashSet<>();

    
}
