package id.ac.tazkia.kinerja.aplikasikinerja.dto;

import lombok.Data;

@Data
public class RekapPengisianKpi {
    private String id;
    private String nama;
    private String department;
    private String area;
    private Long jumlahKpi;
    private Long jumlahKpiTerisi;
}
