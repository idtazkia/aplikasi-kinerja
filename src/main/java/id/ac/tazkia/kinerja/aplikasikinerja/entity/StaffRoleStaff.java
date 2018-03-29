package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class StaffRoleStaff {

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
    @JoinColumn(name = "id_role")
    private StaffRole role;



}
