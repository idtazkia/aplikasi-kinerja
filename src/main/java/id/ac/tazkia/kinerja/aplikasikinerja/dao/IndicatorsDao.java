package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Indicators;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Kpi;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;

public interface IndicatorsDao extends PagingAndSortingRepository<Indicators, String> {
    Indicators findByKpiAndScore(Kpi kpi,BigInteger s);
}