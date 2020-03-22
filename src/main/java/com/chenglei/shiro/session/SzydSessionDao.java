package com.chenglei.shiro.session;

import com.chenglei.shiro.entity.SzydSession;
import com.chenglei.shiro.service.SzydSessionService;
import com.chenglei.shiro.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.util.Collection;

public class SzydSessionDao extends AbstractSessionDAO {

    @Lazy
    @Autowired
    private SzydSessionService szydSessionService;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);//setSessionId
        SzydSession szydSession = new SzydSession();
        szydSession.setSessionId(sessionId.toString());
        szydSession.setSession(SerializableUtils.serialize(session));
        szydSessionService.save(szydSession);
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        SzydSession szydSession = szydSessionService.getSzydSession(sessionId.toString());
        if (szydSession == null) {
            return null;
        }
        return SerializableUtils.deserialize(szydSession.getSession());
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session instanceof ValidatingSession
                && !((ValidatingSession) session).isValid()) {
            return;
        }
        szydSessionService.update(session);

    }

    @Override
    public void delete(Session session) {
        szydSessionService.delete(session);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
