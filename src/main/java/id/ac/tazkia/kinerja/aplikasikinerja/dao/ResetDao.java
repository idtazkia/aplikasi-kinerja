package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.ResetPassword;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResetDao extends PagingAndSortingRepository<ResetPassword, String> {
    ResetPassword findByUser(User u);
}
