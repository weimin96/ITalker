package net.aoliao.web.italker.push.service;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.api.base.ResponseModel;
import net.aoliao.web.italker.push.bean.api.user.UpdateInfoModel;
import net.aoliao.web.italker.push.bean.card.UserCard;
import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * 用户信息处理
 * Created by 你的奥利奥
 * on 2017/8/16
 */
//127.0.0.1/api/user
@Path("/user")
public class UserService extends BaseService {

    //用户信息修改
    @PUT
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        //获取用户信息
        User self = getSelf();
        //更新用户信息
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        //构建需要返回的用户信息
        UserCard userCard = new UserCard(self, true);
        return ResponseModel.buildOk(userCard);
    }
}
