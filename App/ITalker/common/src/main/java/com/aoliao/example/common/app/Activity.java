package com.aoliao.example.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aoliao.example.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author 你的奥利奥
 * @version 2017/8/9
 */

public abstract class Activity extends AppCompatActivity {
    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        if (initArgs(getIntent().getExtras())) {
            int layoutId=getContentLayoutId();
            setContentView(layoutId);
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    protected void initBefore(){

    }

    protected abstract int getContentLayoutId();

    //初始化窗口
    protected void initWindows() {
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数bundle
     * @return 正确返回true，错误返回false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前activity下所有的fragment
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                //判断是否为我们处理的fragment类型
                if (fragment instanceof com.aoliao.example.common.app.Fragment) {
                    //判断是否拦截了返回按钮
                    if (((com.aoliao.example.common.app.Fragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    /**
     * 设置占位布局
     * @param placeHolderView 实现占位布局规范的view
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView){
        mPlaceHolderView=placeHolderView;
    }
}
