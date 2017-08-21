package com.aoliao.example.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.aoliao.example.common.R;

/**
 * @author 你的奥利奥
 * @version 2017/8/19
 */

public abstract class ToolbarActivity extends Activity {
    private Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar){
        mToolbar=toolbar;
        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack(){
        //左上角返回效果
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            // 给左上角图标的左边加上一个返回的图标 。
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    //TODO 待调整
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
