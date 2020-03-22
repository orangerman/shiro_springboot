package com.chenglei.shiro.container;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.chenglei.shiro.realm.SzydRealm;
import com.chenglei.shiro.session.SzydSessionDao;
import com.chenglei.shiro.session.SzydSessionListener;
import com.chenglei.shiro.shiroFilter.SzydOncePerRequestFilter;
import com.chenglei.shiro.util.ValueConstant;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class IOCConfiguration {

    // shiro 核心过滤器
    @Bean(value = "shiroFilter")
    @DependsOn(value = "securityManager")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getSecurityManager());

        shiroFilterFactoryBean.setLoginUrl("/loginPage.html");//没登录 跳转的页面
        shiroFilterFactoryBean.setSuccessUrl("/success.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.html");//没被授权 跳转的页面
        Map<String, String> map = new HashMap<>();

        map.put("/login/desktop", "anon");
        map.put("/login/login", "anon");

        map.put("/login/register", "anon");
        map.put("/login/registering", "anon");

        map.put("/login/once", "once");
//        map.put("/login/list", "user");
        map.put("/login/logout", "anon");
        map.put("/adduser", "roles[user]");
        map.put("/admin", "roles[admin]");

//        map.put("/login/list","roles[viewer]");
//        map.put("/login/register","anon");
//        map.put("/login/register","anon");
//        map.put("/login/register","anon");

        map.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);//拦截url

        Map<String, Filter> filters = getFilters();//自定义shiro拦截器
        shiroFilterFactoryBean.setFilters(filters);


        return shiroFilterFactoryBean;
    }

    @Bean
    public CookieRememberMeManager getCookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey("#{T(org.apache.shiro.codec.Base64).decode('4AvVhmFLUs0KTA3Kprsdag==')}".getBytes());
        cookieRememberMeManager.setCookie(getSimpleCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public Cookie getSimpleCookie() {
        SimpleCookie simpleCookie=new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(1000*3600*24*7);//2592000 30
        return simpleCookie;
    }

    private Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("once", getSzydOncePerRequestFilter());
        return filters;
    }

    private SzydOncePerRequestFilter getSzydOncePerRequestFilter() {
        return new SzydOncePerRequestFilter();
    }

    //subject 管理器
    @Bean(value = "securityManager")
    @DependsOn(value = "authenticator")
    public SecurityManager getSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setAuthenticator(getAuthenticator());
        defaultWebSecurityManager.setRealms(getRealms());
        defaultWebSecurityManager.setSessionManager(getSessionManager());
//        defaultWebSecurityManager.setRememberMeManager(getCookieRememberMeManager());
//        defaultWebSecurityManager.setCacheManager(getCacheManager());
        return defaultWebSecurityManager;
    }

    //缓存管理器
//    @Bean
//    public CacheManager getCacheManager() {
//        EhCacheManager ehCacheManager=new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
//        return ehCacheManager;
//    }

    @Bean
    public SessionManager getSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1000 * 1800);
        sessionManager.setSessionListeners(Arrays.asList(getSessionListeners()));
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setDeleteInvalidSessions(true);//是否开启删除无效的session对象  默认为true
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600*1000);

        sessionManager.setSessionDAO(getSessionDao());
        return sessionManager;
    }

    @Bean
    public SessionDAO getSessionDao() {
        AbstractSessionDAO szydSessionDao = new SzydSessionDao();
        szydSessionDao.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
//        szydSessionDao.setActiveSessionsCacheName("shiro-activeSessionCache");
        return szydSessionDao;
    }

    private SzydSessionListener getSessionListeners() {
        return new SzydSessionListener();
    }

    // realm 获取
    private Collection<Realm> getRealms() {
        Collection<Realm> realms = new ArrayList<Realm>();
        realms.add(getSzydRealm());
        return realms;
    }

    //认证的realm
    @Bean
    public Realm getSzydRealm() {
        AuthorizingRealm logRealm = new SzydRealm();
        logRealm.setCredentialsMatcher(getCredentialsMatcher());
        return logRealm;
    }

    //认证的密码匹配器
    @Bean
    public CredentialsMatcher getCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(ValueConstant.SALTCOUNT);
        return hashedCredentialsMatcher;
    }

    //认证核心入口
    @Bean(value = "authenticator")
    @DependsOn(value = "authenticationStrategy")
    public Authenticator getAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(getAuthenticationStrategy());
        return modularRealmAuthenticator;
    }

    //认证策略
    @Bean(value = "authenticationStrategy")
    public AuthenticationStrategy getAuthenticationStrategy() {
        AtLeastOneSuccessfulStrategy atLeastOneSuccessfulStrategy = new AtLeastOneSuccessfulStrategy();
        return atLeastOneSuccessfulStrategy;
    }

    //调用配置在 Spring IOC 容器中 shiro bean 的生命周期方法
    @Bean(value = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //启用 IOC 容器中使用 shiro 的注解. 但必须在配置了 LifecycleBeanPostProcessor 之后才可以使用
    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(getSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    //配置ShiroDialect，用于Thymeleaf和Shiro标签配合使用
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
