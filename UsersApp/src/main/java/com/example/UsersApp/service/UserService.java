package com.example.UsersApp.service;

import java.util.List;

import com.example.UsersApp.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
