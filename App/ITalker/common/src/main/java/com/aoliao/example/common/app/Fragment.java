package com.aoliao.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 你的奥利奥
 * @version 2017/8/9
 */

public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected Unbinder mRootUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layoutId = getContentLayoutId();
            //初始化当前根布局，但是不在创建时就添加到container
            View viewRoot = inflater.inflate(layoutId, container, false);
            initWidget(viewRoot);
            mRoot = viewRoot;
        } else {
            //有可能activity已经被回收了但fragment还没被回收，先移除再更新数据
            if (mRoot.getParent() != null) {
                //把当前mRoot从其父控件中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {
    }

    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     * @param root
     */
    protected void initWidget(View root) {
        mRootUnbinder=ButterKnife.bind(this,root);
    }

    protected void initData(){}

    /**
     * 返回按键触发
     * @return true 已处理返回逻辑，activity不用finish
     * false 没有处理逻辑，activity自己finish
     */
    public boolean onBackPressed(){
        return false;
    }
}
