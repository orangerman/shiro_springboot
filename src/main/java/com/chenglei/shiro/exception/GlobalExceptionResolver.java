package com.chenglei.shiro.exception;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages={"com.chenglei.shiro"})
public class GlobalExceptionResolver {

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ModelAndView resolveIncorrectCredentialsException(IncorrectCredentialsException e){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("loginPage");
        modelAndView.addObject("loginerror","密码错误");
        return modelAndView;
    }
    @ExceptionHandler(UnknownAccountException.class)
    public ModelAndView resolveUnknownAccountException(UnknownAccountException e){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("loginPage");
        modelAndView.addObject("loginerror","无此用户");
        return modelAndView;
    }
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView resolveAuthorizationException(AuthorizationException e){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("unauthorized");
        modelAndView.addObject("unauthorized","此操作未授权");
        return modelAndView;
    }

}
