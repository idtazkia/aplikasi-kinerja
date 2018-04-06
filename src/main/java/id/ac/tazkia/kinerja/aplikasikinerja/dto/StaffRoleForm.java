package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class StaffRoleForm {

    @NotNull
    private Staff staff;

    @NotNull
    private Set<StaffRole> roles = new HashSet<>();
}
