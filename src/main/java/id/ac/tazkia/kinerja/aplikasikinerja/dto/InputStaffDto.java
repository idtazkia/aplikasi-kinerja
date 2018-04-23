package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.UserPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Data
public class InputStaffDto {
    private String id;

    private String employeeName;
    private String employeeNumber;

    private String jobLevel;

    private String jobTitle;

    private String department;

    private String area;

    private String username;
    private String password;
    private String email;
    private String securityRole;

    private User user;
    private UserPassword userPassword;

    @NotNull
    private Set<StaffRole> roles = new HashSet<>();



}
