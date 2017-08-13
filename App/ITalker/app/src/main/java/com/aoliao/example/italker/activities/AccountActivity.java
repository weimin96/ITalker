package com.aoliao.example.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aoliao.example.common.app.Activity;
import com.aoliao.example.common.app.Fragment;
import com.aoliao.example.italker.R;
import com.aoliao.example.italker.fragments.account.UpdateInfoFragment;

public class AccountActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 跳转到此activity
     * @param context context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
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
