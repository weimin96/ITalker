package com.aoliao.example.italker;

import android.content.Context;
import android.content.Intent;

import com.aoliao.example.common.app.Application;
import com.aoliao.example.factory.Factory;
import com.aoliao.example.italker.activities.AccountActivity;
import com.igexin.sdk.PushManager;

/**
 * @author 你的奥利奥
 * @version 2017/8/13
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }

    @Override
    protected void showAccountView() {
        Intent intent=new Intent();
        intent.setAction("intent.action.LOGIN");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        AccountActivity.show(context);
    }
}
