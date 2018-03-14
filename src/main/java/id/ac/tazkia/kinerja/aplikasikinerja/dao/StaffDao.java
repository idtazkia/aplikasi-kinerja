package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface StaffDao  extends PagingAndSortingRepository<Staff, String> {
    Staff findByUser (User u);

    Page<Staff> findBySuperiorIdOrderByEmployeeName(String staff, Pageable page);
    Page<Staff> findBySuperiorIdAndEmployeeNameContainingIgnoreCaseOrEmployeeNumberContainingIgnoreCaseOrderByEmployeeName(String staff,String name,String nik, Pageable page);

    Page<Staff> findById (String id, Pageable page);

    Long countAllBySuperiorId(String s);

}
