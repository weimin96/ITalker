package com.aoliao.example.factory.presenter.user;

import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/18
 */

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        // 更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        // 回调成功
        void updateSucceed();
    }
}
