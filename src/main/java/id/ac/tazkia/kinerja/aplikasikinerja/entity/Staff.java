package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity @Data
public class Staff {
    @Id
    private String id;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 150)
    private String employeeName;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String employeeNumber;

    @Column(nullable = false)
    @Email
    @NotEmpty
    @NotNull
    private String jobLevel;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String jobTitle;

    @NotNull
    private String department;

    @ManyToMany
    @JoinTable(
            name = "staff_superior",
            joinColumns = @JoinColumn(name = "id_staff"),
            inverseJoinColumns = @JoinColumn(name = "id_superior")
    )
    private Set<Staff> superiors = new HashSet<>();


    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String area;

    @NotNull
    @OneToOne @JoinColumn(name = "id_user")
    private User user;



}
