package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findByStaffAndKpiCategory(Staff s, Category c);
}


