package com.example.UsersApp.repository;

import com.example.UsersApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    User findByName (String name);
    User findBySurname (String surname);
    User findByLogin (String login);
    @Query("select u FROM user u WHERE u.name LIKE %?1% OR u.surname LIKE %?2% OR u.login LIKE %?3%")
    Page<User> findByNameOrSurnameOrLogin(String keyword, String keyword2, String keyword3, Pageable pageable);
   // @Query(value = "select * from user u where u.name like %:keyword% or u.surname like %:keyword% or u.login like %:keyword%", nativeQuery=true)
   // List<User> findByKeyword(@Param("keyword") String keyword);

}
