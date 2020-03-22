package com.chenglei.shiro.realm;

import com.chenglei.shiro.dao.UserDao;
import com.chenglei.shiro.entity.User;
import com.chenglei.shiro.util.RealmUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;

public class SzydRealm extends AuthorizingRealm {
    @Lazy
    @Autowired
    private UserDao userDao;
    //ApplicationContextRegister.getBean(MenuService.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {


        String userName = principals.getPrimaryPrincipal().toString();
        Set<String> rolesInDB=getRolesInDB(userName);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(rolesInDB);

        Set<String> permissions=new HashSet<String>();
        for (String role : rolesInDB) {
            permissions.addAll(getPermissionsByRole(role));
        }
        info.setStringPermissions(permissions);
        return info;
    }

    private Set<String> getPermissionsByRole(String role) {
        Set<String> permissions=new HashSet<String>();
        permissions.add("delete");
        permissions.add("send");
        return permissions;
    }

    private Set<String> getRolesInDB(String userName) {
        Set<String> rolesInDB=new HashSet<String>();
        rolesInDB.add("list");
        rolesInDB.add("update");
        rolesInDB.add("add");
        return rolesInDB;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = getPassword(username);
        //4). 盐值.
        ByteSource credentialsSalt = RealmUtil.getSalt(username);

        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, credentialsSalt, getName());
        return authenticationInfo;
    }

    //db
    private String getPassword(String username) {
        User user=userDao.getUserByUsername(username);
        if(user==null){
            throw new UnknownAccountException("无此用户:"+username);
        }
        return user.getPassword();

    }

    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("user");;
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }

}
