package com.aoliao.example.italker;

import android.os.Bundle;

import com.aoliao.example.common.app.Activity;
import com.aoliao.example.italker.fragments.assist.PermissionFragment;

public class LauncherActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //直到权限申请成功才能显示MainActivity
        if (PermissionFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }

    }
}
