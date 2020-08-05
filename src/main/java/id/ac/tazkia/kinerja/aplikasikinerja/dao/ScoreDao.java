package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.dto.LihatScoreDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.*;
import org.apache.kafka.common.metrics.stats.Count;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ScoreDao extends PagingAndSortingRepository<Score, String> {
    List<Score> findByStaffAndKpiCategoryAndPeriode(Staff staff, Category category, Periode periode);
    List<Score> findByStaffAndPeriode(Staff s,Periode periode);
    List<Score> findByStaffIdAndPeriodeIdAndKpiId(String s, String periode, String kpi);
    List<Score> findByStaffAndPeriodeAndKpi(Staff s, Periode periode, Kpi kpi);
    Score countByKpiAndPeriodeAndStaff(Kpi kpi, Periode periode, Staff staff);


    @Query(value = "select sum(total) as scr, id_category as category, id_staff_role as role, role_name as rname, cname, SUM(weight) AS weight from (select a.*,a.score*f.weight AS total, f.weight,f.id_category,e.id_staff_role,d.role_name ,g.name as cname from score as a inner join staff as b on a.id_staff=b.id inner join staff_role_staff as c on b.id=c.id_staff inner join staff_role as d on c.id_staff_role=d.id inner join staff_role_kpi as e on d.id=e.id_staff_role and a.id_kpi=e.id_kpi inner join kpi as f on a.id_kpi=f.id inner join category as g on f.id_category = g.id where b.id=?1 and id_periode=?2 order by e.id_staff_role)a group by id_staff_role,role_name, id_category, cname", nativeQuery = true)
    List<LihatScoreDto> pisahkanScore(Staff staff, Periode periode);

    @Query(value = "SELECT ROUND((SUM(total)/SUM(weight)*100),2) AS scr, id_staff_role, role_name as rname, SUM(weight) AS weight FROM (SELECT a.*, a.score*f.weight AS total, f.weight, e.id_staff_role, d.role_name FROM score AS a INNER JOIN staff AS b ON a.id_staff = b.id INNER JOIN staff_role_staff AS c ON b.id = c.id_staff INNER JOIN staff_role AS d ON c.id_staff_role = d.id INNER JOIN staff_role_kpi AS e ON d.id = e.id_staff_role AND a.id_kpi = e.id_kpi INNER JOIN kpi AS f ON a.id_kpi = f.id INNER JOIN category AS g ON f.id_category = g.id WHERE b.id =?1 and id_periode=?2 ORDER BY e.id_staff_role) a GROUP BY id_staff_role , rname",nativeQuery = true)
    List<LihatScoreDto> totalAkhir(Staff staff, Periode periode);



}


