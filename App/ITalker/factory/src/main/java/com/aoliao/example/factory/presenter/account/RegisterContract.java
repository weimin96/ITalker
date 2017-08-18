package com.aoliao.example.factory.presenter.account;

import android.support.annotation.StringRes;

import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public interface RegisterContract {
    interface View extends BaseContract.View<Presenter>{

        //注册成功
        void registerSuccess();

    }

    interface Presenter extends BaseContract.Presenter{
        //发起注册
        void register(String phone,String name,String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
