package com.aoliao.example.italker.activities;

import android.content.Intent;

import com.aoliao.example.common.app.Activity;
import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.italker.R;
import com.aoliao.example.italker.fragments.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment=new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();
    }

    /**
     * activity收到剪切图片进行回调
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }
}
