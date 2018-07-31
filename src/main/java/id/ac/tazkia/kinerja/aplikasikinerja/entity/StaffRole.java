package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "roleName")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    @OrderBy
    private Set<Kpi> kpi = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_role_superior")
    private StaffRole superiorRole;

    @NotNull
    private String status;
}
