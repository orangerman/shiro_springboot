package com.chenglei.shiro.service;

import com.chenglei.shiro.base.BaseService;
import com.chenglei.shiro.dao.LoginDao;
import com.chenglei.shiro.entity.User;
import com.chenglei.shiro.util.ValueConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends BaseService {

    @Autowired
    private SecurityManager securityManager;

    @Autowired
    private LoginDao loginDao;

    public boolean login(String username, String password) {
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setRememberMe(true);
            currentUser.login(usernamePasswordToken);
        }
        return true;

    }

    public boolean registering(String username, String password) {
        String hashAlgorithmName = "MD5";
        Object credentials = password;
        Object salt = ByteSource.Util.bytes(username);;
        int hashIterations = ValueConstant.SALTCOUNT;

        String md5password = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
        User user=new User();
        user.setUsername(username);
        user.setPassword(md5password);
        User save = loginDao.save(user);
        return true;
    }

    public boolean list() {
        Subject subject = SecurityUtils.getSubject();
        if(subject.hasRole("list")){//查看者 角色
            return true;
        }else {
            return false;
        }
    }

    public boolean update() {
        return true;
    }

    public boolean delete() {
        return true;
    }

    public boolean add() {
        return true;
    }
}
