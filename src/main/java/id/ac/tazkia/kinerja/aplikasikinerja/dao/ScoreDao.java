package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findAllByStaffKpi_Staff_IdAndStaffKpi_Kpi_Category_id(String id, String a);
}
