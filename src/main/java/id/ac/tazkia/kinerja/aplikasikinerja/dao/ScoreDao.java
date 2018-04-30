package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findByStaffAndKpiCategoryAndPeriode(Staff staff, Category category, Periode periode);
}


