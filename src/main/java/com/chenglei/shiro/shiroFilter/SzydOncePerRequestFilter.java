package com.chenglei.shiro.shiroFilter;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
//https://blog.csdn.net/qq_38930240/article/details/86685389
public class SzydOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("一次请求只调用一次");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
