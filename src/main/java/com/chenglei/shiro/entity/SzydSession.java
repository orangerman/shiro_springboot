package com.chenglei.shiro.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_sessions")
public class SzydSession {

    @Id
    private String sessionId;

    @Lob
    @Column(columnDefinition="TEXT")
    private String session;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
