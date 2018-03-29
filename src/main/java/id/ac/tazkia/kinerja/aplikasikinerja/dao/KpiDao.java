package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface KpiDao extends PagingAndSortingRepository<Kpi, String> {
    Page<Kpi> findByStatusAndKeyResultContainingIgnoreCaseOrderByKeyResult(StatusKpi aktif, String keyresult, Pageable page);
    Page<Kpi> findByStatus(StatusKpi aktif,Pageable page);
}