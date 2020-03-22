package com.chenglei.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
//https://blog.csdn.net/qq_34021712/article/details/80418112
public class SzydSessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {
        System.out.println("onStart...");
    }

    @Override
    public void onStop(Session session) {
        System.out.println("onStop...");
    }

    @Override
    public void onExpiration(Session session) {
        System.out.println("onExpiration...");
    }
}
