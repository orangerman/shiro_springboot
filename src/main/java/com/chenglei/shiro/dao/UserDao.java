package com.chenglei.shiro.dao;

import com.chenglei.shiro.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends PagingAndSortingRepository<User,Integer> {
    @Query(value = "from User u where u.username=:username")
    User getUserByUsername(@Param("username") String username);
}
