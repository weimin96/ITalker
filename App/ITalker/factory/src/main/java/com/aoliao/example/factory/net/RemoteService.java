package com.aoliao.example.factory.net;

import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.AccountRspModel;
import com.aoliao.example.factory.model.api.account.LoginModel;
import com.aoliao.example.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
}
