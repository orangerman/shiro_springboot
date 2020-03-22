package com.chenglei.shiro.threeComponent;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean getShiroFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        DelegatingFilterProxy delegatingFilterProxy=new DelegatingFilterProxy();
        registrationBean.setFilter(delegatingFilterProxy);
        registrationBean.setName("shiroFilter");
        Map<String,String> params=new HashMap<String,String>();
        params.put("targetFilterLifecycle","true");
        registrationBean.setInitParameters(params);
        registrationBean.setUrlPatterns(Arrays.asList("/*"));

        return registrationBean;

    }
}
