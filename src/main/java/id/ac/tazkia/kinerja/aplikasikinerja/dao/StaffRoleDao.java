package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.dto.LihatScoreDto;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface StaffRoleDao extends PagingAndSortingRepository <StaffRole, String> {

    Set<StaffRole> findDistinctBySuperiorRoleIn(Set<StaffRole> roles);
    Page<StaffRole> findByStatusAndAndRoleNameContainingIgnoreCaseOrderByRoleName(String s, String role, Pageable page);
    Page<StaffRole> findByStatus(String s,Pageable page);
    List<StaffRole> findByStatus(String s);
    List<StaffRole> findByKpi(String kpi);

    Long countStaffRoleByStatus(String status);

    @Query(value = "SELECT SUM(weight) AS weight, id_category as cname FROM (SELECT a.*, b.weight, b.id_category FROM staff_role_kpi AS a INNER JOIN kpi AS b ON a.id_kpi = b.id WHERE a.id_staff_role =?1 ORDER BY a.id_staff_role) a GROUP BY id_staff_role, id_category",nativeQuery = true)
    List<LihatScoreDto> bobot(StaffRole staffRole);

    /*@Query( "select count (k) from StaffRole k where k.kpi in :kpi")
    Long findJmlKpi(@Param("kpi") StaffRole staffRole );*/
}
