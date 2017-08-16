package net.aoliao.web.italker.push.provider;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.api.base.ResponseModel;
import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.factory.UserFactory;

import org.glassfish.jersey.server.ContainerRequest;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * 用于所有的接口过滤和拦截
 * Created by 你的奥利奥
 * on 2017/8/16
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    //实现拦截方法
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //检查是否登陆注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login") ||
                relationPath.startsWith("account/register")) {
            //不拦截
            return;
        }
        //从header中找到第一个token
        String token = requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)) {
            final User self = UserFactory.findByToken(token);
            if (self != null) {
                //给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    //主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        //user实现principal接口
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        //默认false，https
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                return;
            }
        }
        //直接返回一个AccountError model
        ResponseModel model = ResponseModel.buildAccountError();
        //构建一个返回
        Response response = Response.status(Response.Status.OK).entity(model).build();
        //拦截，不会走到service中去
        requestContext.abortWith(response);
    }
}
