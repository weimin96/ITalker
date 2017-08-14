package com.aoliao.example.factory;

import com.aoliao.example.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author 你的奥利奥
 * @version 2017/8/13
 */

public class Factory {
    private static final Factory instance;
    private final Executor mExecutor;

    static {
        instance=new Factory();
    }

    private Factory(){
        //新建一个4个线程的线程池
        mExecutor= Executors.newFixedThreadPool(4);
    }
    public static Application app(){
        return Application.getInstance();
    }

    /**
     * 异步运行的方法
     * @param runnable runnable
     */
    public static void runOnAsync(Runnable runnable){
        instance.mExecutor.execute(runnable);
    }
}
