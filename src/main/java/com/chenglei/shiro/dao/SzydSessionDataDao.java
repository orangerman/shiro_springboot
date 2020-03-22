package com.chenglei.shiro.dao;

import com.chenglei.shiro.entity.SzydSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface SzydSessionDataDao extends PagingAndSortingRepository<SzydSession,String> {

    @Modifying
    @Query(value = "update SzydSession s set s.session=:serializeSession where s.sessionId=:sessionId")
    void update(@Param("sessionId") String sessionId,@Param("serializeSession") String serializeSession);
}
