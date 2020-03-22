package com.chenglei.shiro.service;

import com.chenglei.shiro.dao.SzydSessionDataDao;
import com.chenglei.shiro.entity.SzydSession;
import com.chenglei.shiro.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SzydSessionService {

    @Autowired
    private SzydSessionDataDao szydSessionDataDao;

    public void save(SzydSession szydSession) {
        szydSessionDataDao.save(szydSession);
//        int a=9/0;
    }

    public SzydSession getSzydSession(String sessionId) {

        try{
            SzydSession szydSession = szydSessionDataDao.findById(sessionId).get();
            return szydSession;
        }catch (Exception e){
            return null;
        }

    }

    public void update(Session session) {
        szydSessionDataDao.update(session.getId().toString(), SerializableUtils.serialize(session));

    }

    public void delete(Session session) {
        SzydSession szydSession=new SzydSession();
        szydSession.setSessionId(session.getId().toString());
        szydSessionDataDao.delete(szydSession);
    }
}
