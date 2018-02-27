package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.StaffKpi;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StaffKpiDao extends PagingAndSortingRepository<StaffKpi,String> {
    List<StaffKpi> findAllByStaff_IdAndKpi_Category_Id(String id, String category);

    StaffKpi findAllByStaff_EmployeeName(Staff a);
}
