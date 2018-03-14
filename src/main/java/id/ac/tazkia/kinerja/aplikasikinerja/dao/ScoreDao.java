package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface ScoreDao extends PagingAndSortingRepository<Score, String> {

    Page<Score> findById (String id, Pageable page);
    List<Score> findByStaffKpiStaffIdAndStaffKpiKpiCategoryOrderByStaffKpiAsc(String id, Category category);

}


