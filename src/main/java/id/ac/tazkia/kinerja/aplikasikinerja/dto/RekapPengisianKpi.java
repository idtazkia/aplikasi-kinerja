package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import lombok.Data;

@Data
public class RekapPengisianKpi {
    private String id;
    private String nama;
    private String department;
    private String area;
    private String namaRole;
    private Long jumlahKpi;
    private Long jumlahKpiTerisi;
    private String status;
    private Kpi kpi;
    private Staff staff;

}
