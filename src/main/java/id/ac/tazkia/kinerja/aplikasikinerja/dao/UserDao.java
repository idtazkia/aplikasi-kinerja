package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Staff;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserDao extends PagingAndSortingRepository<User, String> {
    User findByUsername(String username);
    User findById (User u);

}
