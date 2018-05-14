package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreCommentDao extends PagingAndSortingRepository<ScoreComment,String> {
    ScoreComment findByAuthorAndPeriodeAndScore(Staff staff, Periode periode, Score score);
    List<ScoreComment> findByAuthorAndPeriodeAndScoreKpiCategory(Staff staff, Periode periode, Category category);
}
