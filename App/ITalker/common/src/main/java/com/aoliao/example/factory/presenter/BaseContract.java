package com.aoliao.example.factory.presenter;

import android.support.annotation.StringRes;

import com.aoliao.example.common.widget.recycler.RecyclerAdapter;

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

        void destroy();
    }

    // 基本的一个列表的View的职责
    interface RecyclerView<T extends Presenter, ViewMode> extends View<T> {
        // 界面端只能刷新整个数据集合，不能精确到每一条数据更新
        // void onDone(List<User> users);

        // 拿到一个适配器，然后自己进行刷新
        RecyclerAdapter<ViewMode> getRecyclerAdapter();

        // 当适配器数据更改了的时候触发
        void onAdapterDataChanged();
    }
}
