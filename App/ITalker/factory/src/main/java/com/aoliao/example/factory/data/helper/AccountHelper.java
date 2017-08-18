package com.aoliao.example.factory.data.helper;

import android.text.TextUtils;

import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.AccountRspModel;
import com.aoliao.example.factory.model.api.account.LoginModel;
import com.aoliao.example.factory.model.api.account.RegisterModel;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.net.NetWork;
import com.aoliao.example.factory.net.RemoteService;
import com.aoliao.example.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public class AccountHelper {
    public static void register(RegisterModel registerModel, final DataSource.Callback<User> callback) {
        //调用retrofit对我们的网络请求接口做代理
        RemoteService service = NetWork.remote();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(registerModel);
        call.enqueue(new AccountRspCallback(callback));
    }

    public static void login(LoginModel loginModel, final DataSource.Callback<User> callback) {
        //调用retrofit对我们的网络请求接口做代理
        RemoteService service = NetWork.remote();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(loginModel);
        call.enqueue(new AccountRspCallback(callback));
    }

    //绑定pushId
    public static void bindPush(DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        // 调用Retrofit对我们的网络请求接口做代理
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求回调部分封装
     */
    public static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            //请求成功返回
            //从返回中拿到全局model，内部是进行Gson解析
            RspModel<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                //拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();
                User user = accountRspModel.getUser();
                //回调callback 数据加载成功
                //保存到数据库中
                user.save();
                //保存自己的信息到xml中，初始化Account数据，用于判断是否已经登录
                Account.login(accountRspModel);
                //如果已经绑定设备
                if (accountRspModel.isBind()) {
                    // 设置绑定状态为True
                    Account.setBind(true);
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                } else {
                    //绑定pushId
                    bindPush(callback);
                }
            } else {
                //进行错误解析
                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }
}
