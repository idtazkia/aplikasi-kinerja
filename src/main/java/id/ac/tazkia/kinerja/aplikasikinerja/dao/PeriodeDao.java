package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PeriodeDao extends PagingAndSortingRepository<Periode,String> {
}
