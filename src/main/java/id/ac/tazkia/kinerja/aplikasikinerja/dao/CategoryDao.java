package id.ac.tazkia.kinerja.aplikasikinerja.dao;

import id.ac.tazkia.kinerja.aplikasikinerja.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryDao extends PagingAndSortingRepository<Category,String>{
}
