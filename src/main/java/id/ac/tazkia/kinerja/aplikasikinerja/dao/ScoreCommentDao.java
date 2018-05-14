package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Periode;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Score;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.ScoreComment;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ScoreCommentDao extends PagingAndSortingRepository<ScoreComment,String> {
    ScoreComment findByAuthorAndPeriodeAndScore(Staff staff, Periode periode, Score score);
}
