package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.constants.AktifConstants;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface StaffDao extends PagingAndSortingRepository<Staff, String> {
    Staff findByUser(User u);
    Page<Staff> findByStatusAndEmployeeNameContainingIgnoreCaseOrderByEmployeeName(String status, String search, Pageable page);
    Page<Staff> findByStatus(String s,Pageable page);
    List<Staff> findByStatus(String s);


    Iterable<Staff> findByRolesAndStatus(StaffRole staffRole, String s);
    Long countStaffByStatus (String status);
}
