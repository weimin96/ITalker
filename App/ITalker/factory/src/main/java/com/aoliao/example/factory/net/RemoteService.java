package com.aoliao.example.factory.net;

import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.AccountRspModel;
import com.aoliao.example.factory.model.api.account.LoginModel;
import com.aoliao.example.factory.model.api.account.RegisterModel;
import com.aoliao.example.factory.model.api.group.GroupCreateModel;
import com.aoliao.example.factory.model.api.group.GroupMemberAddModel;
import com.aoliao.example.factory.model.api.message.MsgCreateModel;
import com.aoliao.example.factory.model.api.user.UserUpdateModel;
import com.aoliao.example.factory.model.card.GroupCard;
import com.aoliao.example.factory.model.card.GroupMemberCard;
import com.aoliao.example.factory.model.card.MessageCard;
import com.aoliao.example.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author 你的奥利奥
 * @version 2017/8/17
 */

public interface RemoteService {

    /**
     * 网络请求的一个注册接口
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     * @param model model
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备id
     * @param pushId 设备Id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true,value ="pushId") String pushId);
    //服务器需要接收加入到请求头里的token，在OKHttp初始化的时候全局拦截添加进来

    /**
     * 用户更新接口
     * @param model model
     * @return RspModel<UserCard>
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    /**
     * 用户搜索接口
     * @param name name
     * @return Call
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    /**
     * 关注某人
     * @param userId userId
     * @return Call
     */
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    /**
     * 获取联系人列表
     * @return Call
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    //查询某人信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    //发送消息的接口
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 我的群的成员列表
    @GET("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 给群添加成员
    @POST("group/{groupId}/member")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);
}
