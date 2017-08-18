package com.aoliao.example.factory.presenter.account;

import android.support.annotation.StringRes;

import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{

        //登陆成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter{
        void login(String phone,  String password);
    }
}
