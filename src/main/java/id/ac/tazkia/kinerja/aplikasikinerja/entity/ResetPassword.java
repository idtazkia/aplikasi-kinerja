package id.ac.tazkia.kinerja.aplikasikinerja.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data @Entity
public class ResetPassword {
    @Id
    @GeneratedValue(generator = "uuid" )
    @NotNull
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_user")
    private User user;

    @NotNull
    private String code;

    @Column(columnDefinition = "DATE")
    @NotNull
    private LocalDate expired;
}
