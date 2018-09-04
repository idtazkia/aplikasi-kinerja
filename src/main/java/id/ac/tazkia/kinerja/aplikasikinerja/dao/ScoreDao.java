package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.apache.kafka.common.metrics.stats.Count;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findByStaffAndKpiCategoryAndPeriode(Staff staff, Category category, Periode periode);
    List<Score> findByStaffAndPeriode(Staff s,Periode periode);
    List<Score> findByStaffIdAndPeriodeIdAndKpiId(String s, String periode, String kpi);
    List<Score> findByStaffAndPeriodeAndKpi(Staff s, Periode periode, Kpi kpi);
    Score countByKpiAndPeriodeAndStaff(Kpi kpi, Periode periode, Staff staff);

}


