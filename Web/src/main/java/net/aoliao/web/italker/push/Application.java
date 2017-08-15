package net.aoliao.web.italker.push;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import net.aoliao.web.italker.push.provider.GsonProvider;
import net.aoliao.web.italker.push.service.AccountService;

import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

/**
 * Created by 你的奥利奥
 * on 2017/8/9
 */
public class Application extends ResourceConfig{
    public Application(){
        //注册逻辑处理的包名
        packages(AccountService.class.getPackage().getName());
        //注册json解析器
//        register(JacksonJsonProvider.class);
        register(GsonProvider.class);
        //注册打印输出
        register(Logger.class);
    }
}
