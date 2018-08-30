package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffRole;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface StaffDao extends PagingAndSortingRepository<Staff, String> {
    Staff findByUser(User u);

    Page<Staff> findByStatusAndEmployeeNameContainingIgnoreCaseOrderByEmployeeName(String status, String search, Pageable page);
    Page<Staff> findByStatus(String s,Pageable page);

    Iterable<Staff> findByRolesAndStatus(StaffRole staffRole, String s);
    Iterable<Staff> countStaffByStatus(String a);
}
