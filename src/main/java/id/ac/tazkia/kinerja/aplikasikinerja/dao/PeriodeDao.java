package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StatusKpi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PeriodeDao extends PagingAndSortingRepository<Periode,String> {
    Periode findByActive (String active);
    Periode findByStatus (StatusKpi statusKpi);
    List<Periode> findByStatusOrderByActiveAsc(StatusKpi s);

    @Query("select p from Periode p where p.status = :status and p.endDate > :tanggal and p.startDate < :tanggal ")
    Periode getCurrentPeriod(@Param("status") StatusKpi status, @Param("tanggal")LocalDate date);
}
