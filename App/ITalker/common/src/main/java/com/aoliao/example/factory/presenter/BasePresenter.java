package com.aoliao.example.factory.presenter;

/**
 * @author 你的奥利奥
 * @version 2017/8/16
 */

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    private T mView;

    public BasePresenter(T view){
        setView(view);
    }

    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        mView=view;
        mView.setPresenter(this);
    }

    //不允许复写
    protected final T getView(){
        return mView;
    }

    @Override
    public void start() {
        T view=mView;
        if (view!=null){
            view.showLoading();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void end() {
        T view=mView;
        mView=null;
        if (view!=null) {
            //把presenter设为NULL
            view.setPresenter(null);
        }
    }
}
