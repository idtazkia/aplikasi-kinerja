package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.User;
import id.ac.tazkia.kinerja.aplikasikinerja.entity.UserPassword;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPasswordDao extends PagingAndSortingRepository<UserPassword,String> {
    UserPassword findByUser (User u);
}
