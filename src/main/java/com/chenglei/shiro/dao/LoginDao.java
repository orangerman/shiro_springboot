package com.chenglei.shiro.dao;

import com.chenglei.shiro.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoginDao extends PagingAndSortingRepository<User,Integer> {

}
