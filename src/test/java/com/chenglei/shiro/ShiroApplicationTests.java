package com.chenglei.shiro;

import com.chenglei.shiro.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroApplicationTests {

    @Autowired
    private DateUtil dateUtil;

    @Test
    void contextLoads() {
        System.out.println(dateUtil.format());
    }

}
