package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Evidence;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface EvidenceDao extends PagingAndSortingRepository<Evidence,String> {
    List<Evidence> findByKpiAndStaffAndPeriode(Kpi kpi, Staff staff, Periode periode);
    List<Evidence> findByKpiAndStaffAndPeriode(Set<Kpi> kpis, Staff staff, Periode periode);

}
