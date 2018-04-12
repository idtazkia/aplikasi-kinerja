package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PeriodeDao extends PagingAndSortingRepository<Periode,String> {
    Periode findByActive (String active);
}
