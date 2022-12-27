package com.example.UsersApp.service;

import java.util.List;
import com.example.UsersApp.entity.User;
import com.example.UsersApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final static int per_page=20;
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void saveUser(User user) {
    }

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.userRepo.findAll(pageable);
    }


    public Page<User> listByPage(int pageNum, int pageSize, String sortField, String sortDir, String keyword)
    {
        Sort sort = Sort.by(sortField);
        sort=sortDir.equals("asc")? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, pageSize, sort);

        if (keyword != null){
            return userRepo.findByNameOrSurnameOrLogin(keyword, keyword, keyword, pageable);
        }
        return userRepo.findAll(pageable);
    }
}
