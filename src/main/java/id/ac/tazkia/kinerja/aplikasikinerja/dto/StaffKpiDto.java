package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class StaffKpiDto {

    @NotNull
    private Set<Kpi> kpi = new HashSet<>();

    @NotNull
    private Set<StaffRole> roles = new HashSet<>();
}
