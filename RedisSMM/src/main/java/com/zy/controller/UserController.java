package com.zy.controller;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zy.entity.User;
import com.zy.service.UserService;
import com.zy.util.RedisTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {
    private final static Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @RequestMapping("/add")
    public  void add(User user){
        int i = userService.addUser(user);
        logger.info("信息");

    }

    @RequestMapping(value = "/findUsers", produces = "application/json; charset=utf-8")
    public void findUsers(HttpServletResponse response,int page,int limit) throws IOException {
        System.out.println("进入findUsers方法");
        ObjectMapper om=new ObjectMapper();
        String key="com.zy.controller.UserController.findUsers";
        //1.先从缓存中获取
        Object value = redisTemplateUtil.getString(key);
        System.out.println(value);
        List<User> users=null;
        Page p = PageHelper.startPage(page, limit);

        if (value==null){
            System.out.println("从数据库获取。。。");
            //2.从数据库获取
            users= userService.findUsers();
            String s = om.writeValueAsString(users);
            redisTemplateUtil.setString(key,s);
            PageInfo<User> pageInfo = new PageInfo<User>(users);
            for (User user : pageInfo.getList()) {
                System.out.println(user);
            }
            StringBuffer  sb=new StringBuffer("{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":[");
            for (User b : users) {
                sb.append("{\"id\":\"" + b.getId()+ "\", \"name\":\"" + b.getName() + "\",\"passwrod\":\"" + b.getPasswrod()+"\"},");
            }
            sb.append("] }");
            sb.deleteCharAt(sb.lastIndexOf(","));
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            System.out.println(sb);
            PrintWriter out = response.getWriter();
            out.print(sb.toString());
            out.flush();
            out.close();
        }else {
            System.out.println("从缓存中读取...");
            users = om.readValue(value.toString(),new TypeReference<List<User>>(){});
            PageInfo<User> pageInfo = new PageInfo<User>(users);
            for (User user : pageInfo.getList()) {
                System.out.println(user);
            }
            StringBuffer  sb=new StringBuffer("{\"code\":0,\"msg\":\"\",\"count\":1000,\"data\":[");
            for (User b : users) {
                sb.append("{\"id\":\"" + b.getId()+ "\", \"name\":\"" + b.getName() + "\",\"passwrod\":\"" + b.getPasswrod()+"\"},");
            }
            sb.append("] }");
            sb.deleteCharAt(sb.lastIndexOf(","));
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            System.out.println(sb);
            PrintWriter out = response.getWriter();

            out.print(sb.toString());
            out.flush();
            out.close();

        }


    }

    @RequestMapping("/deleteUserById")
    public void deleteUserById(HttpServletResponse response,Integer id)throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int i = userService.deleteUserById(id);
        PrintWriter out = response.getWriter();
        out.print(i);
        out.flush();
        out.close();

    }
    @RequestMapping("/updateUserByid")
    public void updateUserByid(HttpServletResponse response,User user) throws  IOException{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        System.out.println("进入修改方法"+user.getId());
        int i = userService.updateUser(user);

    }

}
