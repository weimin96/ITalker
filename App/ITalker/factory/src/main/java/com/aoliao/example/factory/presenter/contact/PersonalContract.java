package com.aoliao.example.factory.presenter.contact;

import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/20
 */

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天或退出登录
        void allowSayHelloOrLogout(boolean isAllowSayHello,boolean isAllowLogout);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
