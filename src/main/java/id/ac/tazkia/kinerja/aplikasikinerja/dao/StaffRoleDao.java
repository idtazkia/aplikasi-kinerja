package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface StaffRoleDao extends PagingAndSortingRepository <Staff , String> {
    Page<Staff> findByEmployeeName(String staff, Pageable page);
}
