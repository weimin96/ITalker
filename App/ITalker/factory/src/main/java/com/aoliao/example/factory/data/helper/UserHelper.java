package com.aoliao.example.factory.data.helper;

import android.net.Network;

import com.aoliao.example.factory.Factory;
import com.aoliao.example.factory.R;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.model.api.RspModel;
import com.aoliao.example.factory.model.api.account.UserUpdateModel;
import com.aoliao.example.factory.model.card.UserCard;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.model.db.User_Table;
import com.aoliao.example.factory.net.NetWork;
import com.aoliao.example.factory.net.RemoteService;
import com.aoliao.example.factory.presenter.contact.FollowPresenter;
import com.aoliao.example.utils.CollectionUtil;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

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
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
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

    //搜索用户
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        //把当前的调度者返回
        return call;
    }

    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.userFollow(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(userCard);
                    //TODO 通知联系人列表刷新
                    //返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    //获取联系人，不需要callback，直接存储到数据库，
    //并通过数据库观察者进行通知界面更新
    //界面更新的时候进行对比，然后差异对比
    public static void refreshContacts() {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<UserCard>>> call = service.userContacts();
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    List<UserCard> cards=rspModel.getResult();
                    if (cards==null||cards.size()==0)
                        return;
                    //唤起进行保存的操作
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(cards,UserCard.class));
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
            }
        });
    }

    // 从本地查询一个用户的信息
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }

    public static User findFromNet(String id) {

        RemoteService remoteService = NetWork.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard userCard = response.body().getResult();
            if (userCard != null) {
                User user = userCard.build();
                Factory.getUserCenter().dispatch(userCard);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索一个用户，优先本地缓存，
     * 没有用然后再从网络拉取
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络查询
     * 没有用然后再从本地缓存拉取
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }
}
