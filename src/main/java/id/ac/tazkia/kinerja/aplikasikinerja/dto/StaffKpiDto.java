package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StaffKpiDto {

    @NotNull
    private String id;

    @NotNull
    private Staff staff;

    @NotNull
    private List<Kpi> kpi;
}
