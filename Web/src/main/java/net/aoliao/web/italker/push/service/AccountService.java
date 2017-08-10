package net.aoliao.web.italker.push.service;

import net.aoliao.web.italker.push.bean.User;

import java.awt.MediaTracker;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by 你的奥利奥
 * on 2017/8/9
 */
//127.0.0.1/api/account
@Path("/account")
public class AccountService {
    //127.0.0.1/api/account/login
    @GET
    @Path("/login")
    public String get(){
        return "You get the login.";
    }

    @POST
    @Path("/login")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User post(){
        User user=new User();
        user.setName("张三");
        user.setSex(1);
        return user;
    }
}
