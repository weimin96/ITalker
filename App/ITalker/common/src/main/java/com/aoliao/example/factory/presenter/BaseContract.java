package com.aoliao.example.factory.presenter;

import android.support.annotation.StringRes;

/**
 * 基本契约
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public interface BaseContract {
    interface View<T extends Presenter>{

        //公共的，显示一个字符串错误
        void showError(@StringRes int str);

        //显示进度条
        void showLoading();

        //设置一个presenter
        void setPresenter(T presenter);

    }

    interface Presenter{

        void start();

        void end();
    }
}
