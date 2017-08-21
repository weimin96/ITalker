package com.aoliao.example.factory.net;

import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.AccountRspModel;
import com.aoliao.example.factory.model.api.account.LoginModel;
import com.aoliao.example.factory.model.api.account.RegisterModel;
import com.aoliao.example.factory.model.api.account.UserUpdateModel;
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

    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);
}
