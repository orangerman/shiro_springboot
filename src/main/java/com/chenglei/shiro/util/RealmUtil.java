package com.chenglei.shiro.util;

import org.apache.shiro.util.ByteSource;

public class RealmUtil {

    public static ByteSource getSalt(String username){
        return  ByteSource.Util.bytes(username);
    }
}
