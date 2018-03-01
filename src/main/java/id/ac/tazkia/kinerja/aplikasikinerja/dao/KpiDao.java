package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface KpiDao extends PagingAndSortingRepository<Kpi, String> {

}