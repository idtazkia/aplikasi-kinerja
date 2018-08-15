package id.ac.tazkia.kinerja.aplikasikinerja.helper;

import id.ac.tazkia.kinerja.aplikasikinerja.dto.KpiTerisi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RekapKpiHelper {
    public static Map<Staff, Long> hitungJumlahKpiTerisi(List<KpiTerisi> listTerisi){
        Map<Staff, Long> jumlahKpiTerisi = new HashMap<>();

        for(KpiTerisi k : listTerisi){
            System.out.println("Staff : "+k.getStaff().getEmployeeName());
            System.out.println("KPI : "+k.getKpi().getKeyResult());
            System.out.println("Jumlah evidence : "+k.getKpiTerisi());

            Long totalKpiTerisi = jumlahKpiTerisi.get(k.getStaff());
            if(totalKpiTerisi == null) {
                totalKpiTerisi = 0L;
            }

            totalKpiTerisi = totalKpiTerisi + 1;
            jumlahKpiTerisi.put(k.getStaff(), totalKpiTerisi);
        }

        /*for(KpiTerisi s : listTerisi){
            System.out.println("Nama : "+s.getStaff().getEmployeeName());
            System.out.println("KPI terisi : "+s.getKpiTerisi());

            jumlahKpiTerisi.keySet();
        }
*/
        return jumlahKpiTerisi;
    }
}
