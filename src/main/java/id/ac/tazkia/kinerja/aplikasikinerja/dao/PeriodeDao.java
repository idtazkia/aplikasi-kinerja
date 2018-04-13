package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PeriodeDao extends PagingAndSortingRepository<Periode,String> {
    Periode findByActive (String active);
    List<Periode> findByStatusOrderByActiveAsc(StatusKpi s);
}
