package com.aoliao.example.italker;

import com.aoliao.example.common.app.Application;
import com.aoliao.example.factory.Factory;
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
}
