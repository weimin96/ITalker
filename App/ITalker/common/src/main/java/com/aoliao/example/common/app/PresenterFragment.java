package com.aoliao.example.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.aoliao.example.factory.presenter.BaseContract;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment
        implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //onAttach时初始化Presenter
        initPresenter();
    }

    protected abstract Presenter initPresenter();
    @Override
    public void showError(@StringRes int str) {
        //显示错误，优先使用占位布局
        if (mPlaceHolderView!=null){
            mPlaceHolderView.triggerError(str);
        }else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter=null;
        }
    }
}
