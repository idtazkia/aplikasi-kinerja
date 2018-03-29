package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class StaffRole {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "role_name", nullable = false)
    @NotEmpty
    private String roleName;

    @Column(nullable = false)
    private String description;

    
}
