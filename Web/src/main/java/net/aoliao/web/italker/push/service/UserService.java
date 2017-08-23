package net.aoliao.web.italker.push.service;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.api.base.PushModel;
import net.aoliao.web.italker.push.bean.api.base.ResponseModel;
import net.aoliao.web.italker.push.bean.api.user.UpdateInfoModel;
import net.aoliao.web.italker.push.bean.card.UserCard;
import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.factory.UserFactory;
import net.aoliao.web.italker.push.utils.PushDispatcher;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    //拉取联系人
    @GET
    @Path("/contact")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        //获取用户信息
        User self = getSelf();
        //拿到我的联系人
        List<User> users = UserFactory.contacts(self);
        // 转换为UserCard
        List<UserCard> userCards = users.stream()
                .map(user -> new UserCard(user, true))
                .collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }

    //关注某人
    @PUT
    @Path("/follow/{followId}")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        //获取用户信息
        User self = getSelf();
        //不能关注自己
        if (self.getId().equalsIgnoreCase(followId) || Strings.isNullOrEmpty(followId)) {
            return ResponseModel.buildParameterError();
        }

        User followUser = UserFactory.findById(followId);
        if (followUser == null) {
            //未找到此人
            return ResponseModel.buildNotFoundUserError(null);
        }

        followUser = UserFactory.follow(self, followUser, null);
        if (followUser == null) {
            //关注失败服务器异常
            return ResponseModel.buildServiceError();
        }
        //TODO 通知我关注的人
        return ResponseModel.buildOk(new UserCard(followUser, true));
    }

    //获取某人信息
    @GET
    @Path("/{id}")
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseModel.buildParameterError();
        }
        //获取用户信息
        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id)) {
            //返回自己
            return ResponseModel.buildOk(new UserCard(self, true));
        }
        User user = UserFactory.findById(id);
        if (user == null) {
            //未找到此人
            return ResponseModel.buildNotFoundUserError(null);
        }
        //如果我们有直接关注的记录，则我已关注需要查询信息的记录
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }

    //搜索人的接口
    @GET
    @Path("/search/{name:(.*)?}")//名字为任意字符，可以为空
    //指定请求与返回的相应体为Json
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        //获取用户信息
        User self = getSelf();
        List<User> searchUsers = UserFactory.search(name);
        final List<User> contacts = UserFactory.contacts(self);

        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    //判断这个人是否是我自己，或者是在我的联系人当中
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId()) ||
                            //进行联系人的任意匹配
                            contacts.stream().anyMatch(contactUser -> contactUser.getId().equalsIgnoreCase(user.getId()));
                    return new UserCard(user, isFollow);

                }).collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }

}
