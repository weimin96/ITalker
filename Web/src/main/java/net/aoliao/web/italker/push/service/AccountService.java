package net.aoliao.web.italker.push.service;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.api.account.AccountRspModel;
import net.aoliao.web.italker.push.bean.api.account.LoginModel;
import net.aoliao.web.italker.push.bean.api.account.RegisterModel;
import net.aoliao.web.italker.push.bean.api.base.ResponseModel;
import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.bean.card.UserCard;
import net.aoliao.web.italker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by 你的奥利奥
 * on 2017/8/9
 */
//127.0.0.1/api/account
@Path("/account")
public class AccountService {

    //登陆
    @POST
    @Path("/login")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (!LoginModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {
            //如果有携带pushId
            if (!Strings.isNullOrEmpty(model.getPushId())){
                return bind(user,model.getPushId());
            }
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            return ResponseModel.buildLoginError();
        }
    }

    @POST
    @Path("/register")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> Register(RegisterModel model) {
        if (!RegisterModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            //已有账户
            return ResponseModel.buildHaveAccountError();
        }
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            //已有用户名
            return ResponseModel.buildHaveNameError();
        }
        //开始注册逻辑
        user = UserFactory.register(model.getAccount(), model.getName(), model.getPassword());
        if (user != null) {
            //如果有携带pushId
            if (!Strings.isNullOrEmpty(model.getPushId())){
                return bind(user,model.getPushId());
            }
            //将AccountRspModel作为信息返回
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            //注册异常
            return ResponseModel.buildRegisterError();
        }
    }

    //绑定设备id
    @POST
    @Path("/bind/{pushId}")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //从请求头中获取token，从url中获取pushId
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {
        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)) {
            return ResponseModel.buildParameterError();
        }
        User user = UserFactory.findByToken(token);
        if (user != null) {
            //设备id绑定
            return bind(user, pushId);
        }  else {
            //账号异常
        return ResponseModel.buildAccountError();
    }

}

    public ResponseModel<AccountRspModel> bind(User self, String pushId) {
        //进行设备id绑定
        User user = UserFactory.bindPushId(self, pushId);
        if (user == null) {
            return ResponseModel.buildServiceError();
        }
        //返回当前账户，并且已经绑定了
        AccountRspModel rspModel = new AccountRspModel(user,true);
        return ResponseModel.buildOk(rspModel);
    }
}
