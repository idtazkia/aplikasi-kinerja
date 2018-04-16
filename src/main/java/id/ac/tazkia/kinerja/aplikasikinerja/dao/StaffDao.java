package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface StaffDao extends PagingAndSortingRepository<Staff, String> {
    Staff findByUser(User u);

    @Query("select s from Staff s where :superior member of s.superiors order by s.employeeName")
    Page<Staff> findSubordinate(@Param("superior") Optional<Staff> superior, Pageable page);

    @Query("select s from Staff s where :superior member of s.superiors")
    List<Staff> test(@Param("superior") Optional<Staff> superior);

    Page<Staff> findBySuperiorsAndEmployeeNameContainingIgnoreCaseOrderByEmployeeName(Optional<Staff> s, String a, Pageable page);

    Page<Staff> findByEmployeeNameContainingIgnoreCaseOrderByEmployeeName(String search,Pageable page);

    Iterable<Staff> findByRolesIn(Set<StaffRole> role);
}
