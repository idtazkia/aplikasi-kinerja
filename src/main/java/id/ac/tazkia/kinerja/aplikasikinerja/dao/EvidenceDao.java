package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.dto.KpiTerisi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Evidence;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface EvidenceDao extends PagingAndSortingRepository<Evidence,String> {
    List<Evidence> findByKpiAndStaffAndPeriode(Kpi kpi, Staff staff, Periode periode);
    List<Evidence> findByKpiAndStaffAndPeriode(Set<Kpi> kpis, Staff staff, Periode periode);
    List<Evidence> findByStaffAndPeriode(Staff staff, Periode periode);

    @Query("select new id.ac.tazkia.kinerja.aplikasikinerja.dto.KpiTerisi(e.staff, e.kpi, count(e.kpi)) " +
            "from Evidence e " +
            "where e.periode = :periode " +
            "group by e.staff, e.kpi ")
    List<KpiTerisi> findKpiTerisi(@Param("periode") Periode p);



}
