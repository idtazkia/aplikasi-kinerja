package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findByStaffAndKpiCategoryAndPeriode(Staff staff, Category category, Periode periode);
}


