package com.chenglei.shiro.controller;

import com.chenglei.shiro.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author fanfan
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password")String password, HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        boolean result=loginService.login(username,password);
        if (result){
            modelAndView.setViewName("index");
            modelAndView.addObject("user","25000");
        }else{
            modelAndView.setViewName("loginPage");

        }
        System.out.println(modelAndView);
        return modelAndView;
    }
    @RequestMapping(value="/desktop",method = RequestMethod.GET)
    public String desktop(){
        return "loginPage";
    }
    @RequestMapping(value="/register",method = RequestMethod.GET)
    public String register(){
       return "register";
    }
    @RequestMapping(value="/registering",method = RequestMethod.POST)
    public String registering(@RequestParam("username") String username, @RequestParam("password")String password){
        loginService.registering(username,password);
       return "loginPage";
    }

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        boolean list=loginService.list();

        return list?"list":"nolist";
    }
    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
//        session.invalidate();
        return "loginPage";
    }
    @RequiresRoles({"update"})
    @RequestMapping(value="/update",method = RequestMethod.GET)
    public String update(){
        boolean update=loginService.update();
        return "update";
    }

    @RequestMapping(value="/delete",method = RequestMethod.GET)
    public String delete(){
        boolean delete=loginService.delete();
        return "delete";
    }
    @RequiresPermissions(value = {"send"})
    @RequestMapping(value="/send",method = RequestMethod.GET)
    public String send(){
        return "send";
    }

    @RequestMapping(value="/add",method = RequestMethod.GET)
    public String add(){
        boolean add=loginService.add();
        return "add";
    }
}
