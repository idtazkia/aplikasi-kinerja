package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface StaffDao  extends PagingAndSortingRepository<Staff, String> {
    Staff findByUser (User u);

    List<Staff> findAllBySuperiorIdOrderByEmployeeName(String staff);

    Page<Staff> findById (String id, Pageable page);

}
