package com.aoliao.example.factory.data.helper;

import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.UserUpdateModel;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.net.NetWork;
import com.aoliao.example.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author 你的奥利奥
 * @version 2017/8/18
 */

public class UserHelper {
    //更新用户信息
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        //调用retrofit对我们的网络请求接口做代理
        RemoteService service = NetWork.remote();
        //得到一个Call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //数据库存储操作，把UserCard转为user
                    //保存用户信息
                    User user = userCard.build();
                    user.save();
                    //返回成功
                    callback.onDataLoaded(userCard);
                } else {
                    //进行错误解析
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                //网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
